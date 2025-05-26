package tools.ivemo.heatmap.io.load;

import color.gradient.Gradient;
import dataset.painter.style.enums.Bucket;
import drmanager.DisplayRangesManager;
import org.w3c.dom.*;
import scheme.AbstractScheme;
import scheme.BlackScheme;
import scheme.WhiteScheme;
import space.Range;
import tools.ivemo.heatmap.io.ILoad;
import tools.ivemo.heatmap.io.params.FrameParams;
import tools.ivemo.heatmap.io.params.PlotParams;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;

/**
 * Loads XML init file (DOM parser).
 *
 * @author MTomczyk
 */

public class LoadInitFile implements ILoad
{
    /**
     * Loaded frame params.
     */
    public FrameParams _FP;

    /**
     * Loaded plot params;
     */
    public PlotParams[] _PP;

    /**
     * Supported schemes for 2D visualization (name -> object mapping).
     */
    private final HashMap<String, AbstractScheme> _supportedSchemes2D;

    /**
     * Supported schemes for 3D visualization (name -> object mapping).
     */
    private final HashMap<String, AbstractScheme> _supportedSchemes3D;

    /**
     * Default scheme object for 2D visualization.
     */
    private final AbstractScheme _defaultScheme2D;

    /**
     * Default scheme object for 3D visualization.
     */
    private final AbstractScheme _defaultScheme3D;

    /**
     * Supported gradients (name -> object mapping).
     */
    private final HashMap<String, Gradient> _supportedGradients;

    /**
     * Default scheme object.
     */
    private final Gradient _defaultGradient;

    /**
     * Default constructor.
     */
    public LoadInitFile()
    {
        _supportedSchemes2D = new HashMap<>(2);
        _supportedSchemes3D = new HashMap<>(2);
        AbstractScheme[] schemes2D = new AbstractScheme[]{new WhiteScheme(), new BlackScheme()};
        AbstractScheme[] schemes3D = new AbstractScheme[]{WhiteScheme.getForPlot3D(), BlackScheme.getForPlot3D()};
        for (AbstractScheme s : schemes2D) _supportedSchemes2D.put(s.getName().toLowerCase(), s);
        for (AbstractScheme s : schemes3D) _supportedSchemes3D.put(s.getName().toLowerCase(), s);
        _defaultScheme2D = new WhiteScheme();
        _defaultScheme3D = WhiteScheme.getForPlot3D();

        _supportedGradients = new HashMap<>(10);
        Gradient[] gradients = new Gradient[]{
                Gradient.getViridisGradient(),
                Gradient.getViridisGradientInverse(),
                Gradient.getMagmaGradient(),
                Gradient.getMagmaGradientInverse(),
                Gradient.getPlasmaGradient(),
                Gradient.getPlasmaGradientInverse(),
                Gradient.getInfernoGradient(),
                Gradient.getInfernoGradientInverse(),
                Gradient.getBlueRedGradient(),
                Gradient.getRedBlueGradient(),
                Gradient.getWhiteBlackGradient(),
                Gradient.getBlackWhiteGradient()};
        for (Gradient g : gradients) _supportedGradients.put(g.getName().toLowerCase(), g);
        _defaultGradient = Gradient.getViridisGradient();
    }


    /**
     * Can be called to save the data file.
     *
     * @param path folder path (relative to the jar; excludes filename)
     * @throws Exception exception
     */
    @Override
    public void load(String path) throws Exception
    {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        File file = new File(path + "init.xml");
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();

        NodeList initList = doc.getElementsByTagName("init");
        if (initList == null) throw new Exception("No init node provided");
        if (initList.getLength() == 0) throw new Exception("No init node provided");

        // parse frame params
        _FP = new FrameParams();

        {
            NodeList frameList = doc.getElementsByTagName("frame");
            if (frameList.getLength() == 0) throw new Exception("No <frame> node");
            Node frameNode = frameList.item(0);

            NamedNodeMap nnm = frameNode.getAttributes();

            {
                Node frameTitle = nnm.getNamedItem("title");
                if (frameTitle == null) throw new Exception("No frame title provided");
                _FP._frameTitle = frameTitle.getNodeValue();
            }

            {
                Node frameSize = nnm.getNamedItem("size");
                if (frameSize == null) throw new Exception("No frame size provided");
                _FP._frameSize = Float.parseFloat(frameSize.getNodeValue());
            }

            {
                Node frameFPS = nnm.getNamedItem("fps");
                if (frameFPS == null) _FP._printFPS = false;
                else _FP._printFPS = Boolean.parseBoolean(frameFPS.getNodeValue());
            }
        }

        {
            NodeList dsList = doc.getElementsByTagName("datasets");
            if (dsList == null) throw new Exception("No data sets provided");
            if (dsList.getLength() == 0) throw new Exception("No data sets provided");

            dsList = doc.getElementsByTagName("dataset");
            if (dsList == null) throw new Exception("No data sets provided");
            if (dsList.getLength() == 0) throw new Exception("No data sets provided");

            _PP = new PlotParams[dsList.getLength()];
            for (int i = 0; i < dsList.getLength(); i++)
                processDataSet(i, dsList);

        }
    }

    /**
     * Supportive method processing one data set.
     *
     * @param i      index of the currently processed DS
     * @param dsList node list containing nodes linked do data sets
     * @throws Exception exception can be thrown (e.g., when the input data is invalid)
     */
    protected void processDataSet(int i, NodeList dsList) throws Exception
    {
        _PP[i] = new PlotParams();

        Node dsNode = dsList.item(i);
        NamedNodeMap nnm = dsNode.getAttributes();

        {
            Node dimensions = nnm.getNamedItem("dimensions");
            if (dimensions == null) throw new Exception("No number of dimensions for data set provided");
            _PP[i]._dimensions = Integer.parseInt(dimensions.getNodeValue());
        }

        {
            Node file = nnm.getNamedItem("file");
            if (file == null) throw new Exception("No file name for data set provided");
            _PP[i]._fileName = file.getNodeValue();
        }

        {
            Node ms = nnm.getNamedItem("ms");
            if (ms != null)
            {
                String s = ms.getNodeValue();

                if (_PP[i]._dimensions == 2) _PP[i]._bucketStyle = Bucket.SQUARE_2D;
                else
                {
                    _PP[i]._bucketStyle = Bucket.CUBE_3D;
                    if (s.equals("p"))
                    {
                        _PP[i]._bucketStyle = Bucket.POINT_3D;
                        // conditionally check point size
                        Node ps = nnm.getNamedItem("ps");
                        if (ps != null) _PP[i]._pointSize = Float.parseFloat(ps.getNodeValue());
                    }
                }
            } else
            {
                if (_PP[i]._dimensions == 2) _PP[i]._bucketStyle = Bucket.SQUARE_2D;
                else _PP[i]._bucketStyle = Bucket.CUBE_3D;
            }
        }

        {
            Node scheme = nnm.getNamedItem("scheme");
            if (scheme != null)
            {
                if (_PP[i]._dimensions == 2)
                {
                    String s = scheme.getNodeValue();
                    AbstractScheme candidate = _supportedSchemes2D.get(s);
                    if (candidate == null) _PP[i]._scheme = _defaultScheme2D.getClone();
                    else _PP[i]._scheme = candidate.getClone();
                } else if (_PP[i]._dimensions == 3)
                {
                    String s = scheme.getNodeValue();
                    AbstractScheme candidate = _supportedSchemes3D.get(s);
                    if (candidate == null) _PP[i]._scheme = _defaultScheme3D.getClone();
                    else _PP[i]._scheme = candidate.getClone();
                }
            } else
            {
                if (_PP[i]._dimensions == 2) _PP[i]._scheme = _defaultScheme2D.getClone();
                else if (_PP[i]._dimensions == 3) _PP[i]._scheme = _defaultScheme3D.getClone();
            }
        }


        {
            Node title = nnm.getNamedItem("title");
            if (title != null) _PP[i]._title = title.getNodeValue();
            else _PP[i]._title = null;
        }

        if (!(dsNode instanceof Element DSElement))
            throw new Exception("Casting exception (axes) in dataset");

        NodeList axes = DSElement.getElementsByTagName("axes");
        Node axesNode = axes.item(0);

        if (!(axesNode instanceof Element AxesElement))
            throw new Exception("Casting exception (axes) in dataset");
        axes = AxesElement.getElementsByTagName("axis");

        if (axes.getLength() != _PP[i]._dimensions)
            throw new Exception("Number of axes differs the number of dimensions in dataset");

        boolean[] typesUsed = new boolean[3];

        if (_PP[i]._dimensions == 2) _PP[i]._pDisplayRangesManager = DisplayRangesManager.Params.getFor2D(null, null);
        else _PP[i]._pDisplayRangesManager = DisplayRangesManager.Params.getFor3D(null, null, null);


        for (int a = 0; a < axes.getLength(); a++)
        {
            Node axis = axes.item(a);
            NamedNodeMap nnmA = axis.getAttributes();

            String t;
            int axType = 0;
            {
                Node type = nnmA.getNamedItem("type");
                if (type == null) throw new Exception("No axis type provided in dataset");
                t = type.getNodeValue();
                if (t.equals("y"))
                {
                    axType = 1;
                    typesUsed[1] = true;
                } else if (t.equals("z"))
                {
                    axType = 2;
                    typesUsed[2] = true;
                } else typesUsed[0] = true;
            }

            {
                Node title = nnmA.getNamedItem("title");
                if (title != null)
                {
                    if (axType == 0) _PP[i]._xAxisTitle = title.getNodeValue();
                    else if (axType == 1) _PP[i]._yAxisTitle = title.getNodeValue();
                    else _PP[i]._zAxisTitle = title.getNodeValue();
                } else
                {
                    if (axType == 0) _PP[i]._xAxisTitle = null;
                    else if (axType == 1) _PP[i]._yAxisTitle = null;
                    else _PP[i]._zAxisTitle = null;
                }
            }

            {
                Node div = nnmA.getNamedItem("divisions");
                if (div == null)
                    throw new Exception("No axis (" + t + " axis) number of divisions provided in dataset");
                int d = Integer.parseInt(div.getNodeValue());
                if (axType == 0) _PP[i]._xAxisDivisions = d;
                else if (axType == 1) _PP[i]._yAxisDivisions = d;
                else _PP[i]._zAxisDivisions = d;
            }

            // calculate display bound
            double gMin;
            double gMax;

            {
                Node min = nnmA.getNamedItem("min");
                if (min == null) throw new Exception("No axis (" + t + " axis) min value provided in dataset");
                gMin = Double.parseDouble(min.getNodeValue());

                Node max = nnmA.getNamedItem("max");
                if (max == null) throw new Exception("No axis (" + t + " axis) max value provided in dataset");
                gMax = Double.parseDouble(max.getNodeValue());
            }

            if (Double.compare(gMin, gMax) >= 0)
                throw new Exception("Invalid axis bounds (" + t + " axis) in dataset provided: (" + gMin + ";" + gMax + ")");


            if (axType == 0) _PP[i]._pDisplayRangesManager._DR[0].setR(new Range(gMin, gMax));
            else if (axType == 1) _PP[i]._pDisplayRangesManager._DR[1].setR(new Range(gMin, gMax));
            else _PP[i]._pDisplayRangesManager._DR[2].setR(new Range(gMin, gMax));
        }

        checkTypes(i, typesUsed);

        NodeList heatmap = DSElement.getElementsByTagName("heatmap");
        if (heatmap.getLength() == 0) throw new Exception("No data on heatmap provided in dataset");
        Node heatmapNode = heatmap.item(0);

        {
            NamedNodeMap nnmHeatmap = heatmapNode.getAttributes();
            Node gradientNode = nnmHeatmap.getNamedItem("gradient");
            if (gradientNode == null)
                throw new Exception("No gradient for heatmap in data set provided");
            String grad = gradientNode.getNodeValue().toLowerCase();
            _PP[i]._gradient = _supportedGradients.getOrDefault(grad, _defaultGradient).getClone();

            double gMin;
            double gMax;

            Node title = nnmHeatmap.getNamedItem("title");
            if (title != null) _PP[i]._heatmapTitle = title.getNodeValue();

            Node min = nnmHeatmap.getNamedItem("min");
            if (min == null) throw new Exception("No heatmap min value provided in dataset");
            gMin = Double.parseDouble(min.getNodeValue());

            Node max = nnmHeatmap.getNamedItem("max");
            if (max == null) throw new Exception("No heatmap max value provided in dataset");
            gMax = Double.parseDouble(max.getNodeValue());

            if (Double.compare(gMin, gMax) >= 0)
                throw new Exception("Invalid heatmap bounds in dataset provided: (" + gMin + ";" + gMax + ")");


            _PP[i]._heatmapDisplayRange = new Range(gMin, gMax);
        }
    }

    /**
     * Checks whether the dimensionality matches axis data.
     *
     * @param i         data set id
     * @param typesUsed flags indicating which axes were successfully parsed ([x, y, z]).
     * @throws Exception exception can be thrown (e.g., when the input data is invalid)
     */
    protected void checkTypes(int i, boolean[] typesUsed) throws Exception
    {
        if (_PP[i]._dimensions == 2)
        {
            if (!typesUsed[0]) throw new Exception("No X-axis data provided in #" + (i + 1) + " dataset");
            if (!typesUsed[1]) throw new Exception("No Y-axis data provided in #" + (i + 1) + " dataset");
            if (typesUsed[2]) throw new Exception("Redundant Z-axis data provided in #" + (i + 1) + " dataset");
        } else
        {
            if (!typesUsed[0]) throw new Exception("No X-axis data provided in #" + (i + 1) + " dataset");
            if (!typesUsed[1]) throw new Exception("No Y-axis data provided in #" + (i + 1) + " dataset");
            if (!typesUsed[2]) throw new Exception("No Z-axis data provided in #" + (i + 1) + " dataset");
        }
    }

}

