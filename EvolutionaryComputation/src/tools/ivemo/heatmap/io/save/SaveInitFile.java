package tools.ivemo.heatmap.io.save;

import color.gradient.Gradient;
import dataset.painter.style.enums.Bucket;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import scheme.BlackScheme;
import scheme.WhiteScheme;
import tools.ivemo.heatmap.io.ISave;
import tools.ivemo.heatmap.io.params.FrameParams;
import tools.ivemo.heatmap.io.params.PlotParams;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Generates XML init file (DOM parser).
 *
 * @author MTomczyk
 */

public class SaveInitFile implements ISave
{
    /**
     * Main frame params.
     */
    protected FrameParams _frameParams;

    /**
     * Plot params.
     */
    protected PlotParams[] _plotParams;

    /**
     * Allowed scheme names (white for default).
     */
    protected Set<String> _schemeNames;

    /**
     * Default scheme name (white).
     */
    protected String _defaultSchemeName;

    /**
     * Allowed gradient names (viridis for default).
     */
    protected Set<String> _gradientNames;

    /**
     * Default gradient name (viridis).
     */
    protected String _defaultGradientName;

    /**
     * Parameterized constructor.
     *
     * @param frameParams main frame params
     * @param plotParams  plot params
     */
    public SaveInitFile(FrameParams frameParams, PlotParams plotParams)
    {
        this(frameParams, new PlotParams[]{plotParams});
    }

    /**
     * Parameterized constructor.
     *
     * @param frameParams main frame params
     * @param plotParams  plot params
     */
    public SaveInitFile(FrameParams frameParams, PlotParams [] plotParams)
    {
        _frameParams = frameParams;
        _plotParams = plotParams;

        {
            _schemeNames = new HashSet<>(10);
            _schemeNames.add(new WhiteScheme().getName().toLowerCase());
            _schemeNames.add(new BlackScheme().getName().toLowerCase());

            _defaultSchemeName = new WhiteScheme().getName().toLowerCase();
        }

        {
            _gradientNames = new HashSet<>(10);
            _gradientNames.add(Gradient.getMagmaGradient().getName().toLowerCase());
            _gradientNames.add(Gradient.getMagmaGradientInverse().getName().toLowerCase());
            _gradientNames.add(Gradient.getViridisGradient().getName().toLowerCase());
            _gradientNames.add(Gradient.getViridisGradientInverse().getName().toLowerCase());
            _gradientNames.add(Gradient.getPlasmaGradient().getName().toLowerCase());
            _gradientNames.add(Gradient.getPlasmaGradientInverse().getName().toLowerCase());
            _gradientNames.add(Gradient.getInfernoGradient().getName().toLowerCase());
            _gradientNames.add(Gradient.getInfernoGradientInverse().getName().toLowerCase());
            _gradientNames.add(Gradient.getBlackWhiteGradient().getName().toLowerCase());
            _gradientNames.add(Gradient.getWhiteBlackGradient().getName().toLowerCase());
            _gradientNames.add(Gradient.getRedBlueGradient().getName().toLowerCase());
            _gradientNames.add(Gradient.getBlueRedGradient().getName().toLowerCase());
            _defaultGradientName = Gradient.getViridisGradient().getName().toLowerCase();
        }
    }

    /**
     * Can be called to save the XML init file.
     *
     * @param path path (relative to the jar)
     * @throws Exception exception
     */
    @Override
    public void save(String path) throws Exception
    {
        if (_frameParams == null) throw new Exception("There are no frame params provided");
        if (_plotParams == null) throw new Exception("There are no plot params provided");

        String filePath = path + "init.xml";
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        Document doc = builder.newDocument();
        Element root = doc.createElement("init");
        doc.appendChild(root);

        Element frame = doc.createElement("frame");
        frame.setAttribute("title", _frameParams._frameTitle);
        frame.setAttribute("size", Float.toString(_frameParams._frameSize));
        frame.setAttribute("fps", Boolean.toString(_frameParams._printFPS));
        root.appendChild(frame);

        Element datasets = doc.createElement("datasets");
        root.appendChild(datasets);

        for (PlotParams plotParam : _plotParams)
        {
            if (plotParam == null) throw new Exception("Plot param is null");

            Element dataset = doc.createElement("dataset");
            processDataSet(doc, dataset, plotParam);
            datasets.appendChild(dataset);
        }

        DOMSource dom = new DOMSource(doc);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(dom, result);
    }

    /**
     * Aux method processing the heatmap data.
     *
     * @param doc    document object
     * @param dsNode heatmap data set node
     * @param pp     plot params linked to the heatmap processor
     */
    protected void processDataSet(Document doc, Element dsNode, PlotParams pp)
    {
        if (pp._title != null)
            dsNode.setAttribute("title", pp._title);

        dsNode.setAttribute("dimensions", Integer.toString(pp._dimensions));
        dsNode.setAttribute("file", pp._fileName);


        // scheme
        {
            if (pp._scheme != null)
            {
                String sn = pp._scheme.getName().toLowerCase();
                if (!_schemeNames.contains(sn)) dsNode.setAttribute("scheme", _defaultSchemeName);
                else dsNode.setAttribute("scheme", sn);
            }
        }

        // marker style
        {
            if (pp._bucketStyle != null)
            {
                String ms = "s";
                if ((pp._bucketStyle == Bucket.POINT_3D) && (pp._dimensions == 3))
                {
                    dsNode.setAttribute("ps", Float.toString(pp._pointSize));
                    ms = "p";
                }
                dsNode.setAttribute("ms", ms);

            }
        }

        Element axes = doc.createElement("axes");
        dsNode.appendChild(axes);

        String [] axesTypes = new String[] {"x", "y", "z"};
        String [] axesTitles = new String[] {pp._xAxisTitle, pp._yAxisTitle, pp._zAxisTitle};

        int [] axesDivisions = new int [] {pp._xAxisDivisions, pp._yAxisDivisions, pp._zAxisDivisions};
        for (int i = 0; i < pp._dimensions; i++)
        {
            Element axis = doc.createElement("axis");
            axis.setAttribute("type", axesTypes[i]);
            axis.setAttribute("title", axesTitles[i]);
            axis.setAttribute("divisions", Integer.toString(axesDivisions[i]));
            axis.setAttribute("min", Double.toString(pp._pDisplayRangesManager._DR[i].getR().getLeft()));
            axis.setAttribute("max", Double.toString(pp._pDisplayRangesManager._DR[i].getR().getRight()));
            axes.appendChild(axis);
        }

        Element heatmap = doc.createElement("heatmap");
        if (pp._heatmapTitle != null)
            heatmap.setAttribute("title", pp._heatmapTitle);

        heatmap.setAttribute("min", Double.toString(pp._heatmapDisplayRange.getLeft()));
        heatmap.setAttribute("max", Double.toString(pp._heatmapDisplayRange.getRight()));
        {
            String gn = pp._gradient.getName().toLowerCase();
            if (!_gradientNames.contains(gn)) heatmap.setAttribute("gradient", _defaultGradientName);
            else heatmap.setAttribute("gradient", gn);
        }
        dsNode.appendChild(heatmap);
    }
}

