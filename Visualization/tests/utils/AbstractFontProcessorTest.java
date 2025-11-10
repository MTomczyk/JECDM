package utils;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.font.TextAttribute;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides various tests for {@link FontProcessor}.
 *
 * @author MTomczyk
 */
class AbstractFontProcessorTest
{
    /**
     * Test 1.
     */
    @Test
    void parseTextDependentAttributes()
    {
        {
            FontProcessor font = new FontProcessor();
            assertNull(font.parseTextDependentState(null, 10.0f, null));
            assertNull(font.parseTextDependentState("", 10.0f, null));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("1", 10.0f, null);
            assertEquals(0, TDA._textAttributes.size());
            assertEquals("1", TDA._text);
            assertEquals("1", TDA._parsed);
        }

        {
            FontProcessor font = new FontProcessor();
            assertNull(font.parseTextDependentState(null, 10.0f, null));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("TEST", 10.0f, null);
            assertEquals(0, TDA._textAttributes.size());
            assertEquals("TEST", TDA._text);
            assertEquals("TEST", TDA._parsed);
        }

        {
            FontProcessor font = new FontProcessor();
            assertNull(font.parseTextDependentState(null, 10.0f, null));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\", 10.0f, null);
            assertEquals(0, TDA._textAttributes.size());
            assertEquals("\\", TDA._text);
            assertEquals("", TDA._parsed);
        }

        {
            FontProcessor font = new FontProcessor();
            assertNull(font.parseTextDependentState(null, 10.0f, null));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\\\", 10.0f, null);
            assertEquals(0, TDA._textAttributes.size());
            assertEquals("\\\\", TDA._text);
            assertEquals("\\", TDA._parsed);
        }

        {
            FontProcessor font = new FontProcessor();
            assertNull(font.parseTextDependentState(null, 10.0f, null));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\\\\\", 10.0f, null);
            assertEquals(0, TDA._textAttributes.size());
            assertEquals("\\\\\\", TDA._text);
            assertEquals("\\", TDA._parsed);
        }

        {
            FontProcessor font = new FontProcessor();
            assertNull(font.parseTextDependentState(null, 10.0f, null));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\a\\\\b", 10.0f, null);
            assertEquals(0, TDA._textAttributes.size());
            assertEquals("\\a\\\\b", TDA._text);
            assertEquals("a\\b", TDA._parsed);
        }

        {
            FontProcessor font = new FontProcessor();
            assertNull(font.parseTextDependentState(null, 10.0f, null));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\a\\b\\c", 10.0f, null);
            assertEquals(0, TDA._textAttributes.size());
            assertEquals("\\a\\b\\c", TDA._text);
            assertEquals("abc", TDA._parsed);
        }
        {
            FontProcessor font = new FontProcessor();
            assertNull(font.parseTextDependentState(null, 10.0f, null));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("abc\\\\\\", 10.0f, null);
            assertEquals(0, TDA._textAttributes.size());
            assertEquals("abc\\\\\\", TDA._text);
            assertEquals("abc\\", TDA._parsed);
        }
        {
            FontProcessor font = new FontProcessor();
            assertNull(font.parseTextDependentState(null, 10.0f, null));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("TE\\$ST", 10.0f, null);
            assertEquals(0, TDA._textAttributes.size());
            assertEquals("TE\\$ST", TDA._text);
            assertEquals("TE$ST", TDA._parsed);
        }
        {
            FontProcessor font = new FontProcessor();
            assertNull(font.parseTextDependentState(null, 10.0f, null));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("TE\\$ST\\$", 10.0f, null);
            assertEquals(0, TDA._textAttributes.size());
            assertEquals("TE\\$ST\\$", TDA._text);
            assertEquals("TE$ST$", TDA._parsed);
        }
        {
            FontProcessor font = new FontProcessor();
            font._font = new Font("Times New Roman", Font.PLAIN, 10);
            assertNull(font.parseTextDependentState(null, 10.0f, null));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("TE\\$ST$", 10.0f, null);
            assertEquals(0, TDA._textAttributes.size());
            assertEquals("TE\\$ST$", TDA._text);
            assertEquals("TE$ST", TDA._parsed);
        }
        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            assertNull(font.parseTextDependentState(null, 10.0f, null));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("TE$ST\\$", 10.0f, null);
            assertEquals(0, TDA._textAttributes.size());
            assertEquals("TE$ST\\$", TDA._text);
            assertEquals("TEST$", TDA._parsed);
        }
        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            assertNull(font.parseTextDependentState(null, 10.0f, null));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("TE\\$ST\\$", 10.0f, null);
            assertEquals(0, TDA._textAttributes.size());
            assertEquals("TE\\$ST\\$", TDA._text);
            assertEquals("TE$ST$", TDA._parsed);
        }
        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            assertNull(font.parseTextDependentState(null, 10.0f, null));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\TE$$ST", 10.0f, null);
            assertEquals(0, TDA._textAttributes.size());
            assertEquals("\\TE$$ST", TDA._text);
            assertEquals("TEST", TDA._parsed);
        }
        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            assertNull(font.parseTextDependentState(null, 10.0f, null));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\TE$ST$", 10.0f, null);
            assertEquals(1, TDA._textAttributes.size());
            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals(new Font("Times New Roman", Font.ITALIC, 10), TDA._textAttributes.getFirst()._value);
            assertEquals(2, TDA._textAttributes.getFirst()._bi);
            assertEquals(4, TDA._textAttributes.getFirst()._ei);
            assertEquals("\\TE$ST$", TDA._text);
            assertEquals("TEST", TDA._parsed);
        }

        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\TE$S\\T$", 10.0f, null);
            assertEquals(1, TDA._textAttributes.size());
            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals(new Font("Times New Roman", Font.ITALIC, 10), TDA._textAttributes.getFirst()._value);
            assertEquals(2, TDA._textAttributes.getFirst()._bi);
            assertEquals(4, TDA._textAttributes.getFirst()._ei);
            assertEquals("\\TE$S\\T$", TDA._text);
            assertEquals("TEST", TDA._parsed);
        }

        //========================


        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\$$TE_ST$", 10.0f, null);
            assertEquals(2, TDA._textAttributes.size());

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals(new Font("Times New Roman", Font.ITALIC, 10), TDA._textAttributes.getFirst()._value);
            assertEquals(1, TDA._textAttributes.getFirst()._bi);
            assertEquals(5, TDA._textAttributes.getFirst()._ei);

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getLast()._attribute);
            assertEquals(AbstractFontProcessor.IMPOSE_SUBSCRIPT, TDA._textAttributes.getLast()._imposedOperation);
            assertEquals(3, TDA._textAttributes.getLast()._bi);
            assertEquals(4, TDA._textAttributes.getLast()._ei);

            assertEquals("\\$$TE_ST$", TDA._text);
            assertEquals("$TEST", TDA._parsed);
        }
        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\$$TE\\_ST$", 10.0f, null);
            assertEquals(1, TDA._textAttributes.size());
            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getFirst()._value.toString());
            assertEquals(1, TDA._textAttributes.getFirst()._bi);
            assertEquals(6, TDA._textAttributes.getFirst()._ei);
            assertEquals("\\$$TE\\_ST$", TDA._text);
            assertEquals("$TE_ST", TDA._parsed);
        }

        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\$$TEST_$", 10.0f, null);
            assertEquals(1, TDA._textAttributes.size());
            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getFirst()._value.toString());
            assertEquals(5, TDA._textAttributes.getFirst()._bi);
            assertEquals(6, TDA._textAttributes.getFirst()._ei);
            assertEquals("\\$$TEST_$", TDA._text);
            assertEquals("$TEST$", TDA._parsed);

        }
        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\\\$TE$S_T_", 10.0f, null);
            assertEquals(2, TDA._textAttributes.size());
            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getFirst()._value.toString());
            assertEquals(1, TDA._textAttributes.getFirst()._bi);
            assertEquals(3, TDA._textAttributes.getFirst()._ei);

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getLast()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=plain,size=10]",
                    TDA._textAttributes.getLast()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_SUBSCRIPT, TDA._textAttributes.getLast()._imposedOperation);
            assertEquals(4, TDA._textAttributes.getLast()._bi);
            assertEquals(5, TDA._textAttributes.getLast()._ei);

            assertEquals("\\\\$TE$S_T_", TDA._text);
            assertEquals("\\TEST", TDA._parsed);
        }

        //====
        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\$$TE_{ST}$", 10.0f, null);
            assertEquals(2, TDA._textAttributes.size());

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getFirst()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_ITALIC,
                    TDA._textAttributes.getFirst()._imposedOperation);
            assertEquals(1, TDA._textAttributes.getFirst()._bi);
            assertEquals(5, TDA._textAttributes.getFirst()._ei);

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getLast()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getLast()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_SUBSCRIPT,
                    TDA._textAttributes.getLast()._imposedOperation);
            assertEquals(3, TDA._textAttributes.getLast()._bi);
            assertEquals(5, TDA._textAttributes.getLast()._ei);


            assertEquals("\\$$TE_{ST}$", TDA._text);
            assertEquals("$TEST", TDA._parsed);
        }
        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\$$TE_{S}T$", 10.0f, null);
            assertEquals(2, TDA._textAttributes.size());

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getFirst()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_ITALIC,
                    TDA._textAttributes.getFirst()._imposedOperation);
            assertEquals(1, TDA._textAttributes.getFirst()._bi);
            assertEquals(5, TDA._textAttributes.getFirst()._ei);

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getLast()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getLast()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_SUBSCRIPT,
                    TDA._textAttributes.getLast()._imposedOperation);
            assertEquals(3, TDA._textAttributes.getLast()._bi);
            assertEquals(4, TDA._textAttributes.getLast()._ei);

            assertEquals("\\$$TE_{S}T$", TDA._text);
            assertEquals("$TEST", TDA._parsed);
        }
        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\$$TE_{}ST$", 10.0f, null);
            assertEquals(1, TDA._textAttributes.size());

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getFirst()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_ITALIC,
                    TDA._textAttributes.getFirst()._imposedOperation);
            assertEquals(1, TDA._textAttributes.getFirst()._bi);
            assertEquals(5, TDA._textAttributes.getFirst()._ei);

            assertEquals("\\$$TE_{}ST$", TDA._text);
            assertEquals("$TEST", TDA._parsed);
        }

        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\$$TE_{S\\\\}T$", 10.0f, null);
            assertEquals(2, TDA._textAttributes.size());

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getFirst()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_ITALIC,
                    TDA._textAttributes.getFirst()._imposedOperation);
            assertEquals(1, TDA._textAttributes.getFirst()._bi);
            assertEquals(6, TDA._textAttributes.getFirst()._ei);

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getLast()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getLast()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_SUBSCRIPT,
                    TDA._textAttributes.getLast()._imposedOperation);
            assertEquals(3, TDA._textAttributes.getLast()._bi);
            assertEquals(5, TDA._textAttributes.getLast()._ei);

            assertEquals("\\$$TE_{S\\\\}T$", TDA._text);
            assertEquals("$TES\\T", TDA._parsed);
        }
        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\$$TE_{S\\}T$", 10.0f, null);
            assertEquals(0, TDA._textAttributes.size());
            assertEquals("\\$$TE_{S\\}T$", TDA._text);
            assertEquals("$TEST$", TDA._parsed);
        }

        // ======================

        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\$$TE^ST$", 10.0f, null);
            assertEquals(2, TDA._textAttributes.size());

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals(new Font("Times New Roman", Font.ITALIC, 10), TDA._textAttributes.getFirst()._value);
            assertEquals(1, TDA._textAttributes.getFirst()._bi);
            assertEquals(5, TDA._textAttributes.getFirst()._ei);

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getLast()._attribute);
            assertEquals(AbstractFontProcessor.IMPOSE_SUPERSCRIPT, TDA._textAttributes.getLast()._imposedOperation);
            assertEquals(3, TDA._textAttributes.getLast()._bi);
            assertEquals(4, TDA._textAttributes.getLast()._ei);

            assertEquals("\\$$TE^ST$", TDA._text);
            assertEquals("$TEST", TDA._parsed);
        }
        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\$$TE\\^ST$", 10.0f, null);
            assertEquals(1, TDA._textAttributes.size());
            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getFirst()._value.toString());
            assertEquals(1, TDA._textAttributes.getFirst()._bi);
            assertEquals(6, TDA._textAttributes.getFirst()._ei);
            assertEquals("\\$$TE\\^ST$", TDA._text);
            assertEquals("$TE^ST", TDA._parsed);
        }

        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\$$TEST^$", 10.0f, null);
            assertEquals(1, TDA._textAttributes.size());
            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getFirst()._value.toString());
            assertEquals(5, TDA._textAttributes.getFirst()._bi);
            assertEquals(6, TDA._textAttributes.getFirst()._ei);
            assertEquals("\\$$TEST^$", TDA._text);
            assertEquals("$TEST$", TDA._parsed);

        }
        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\\\$TE$S^T^", 10.0f, null);
            assertEquals(2, TDA._textAttributes.size());
            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getFirst()._value.toString());
            assertEquals(1, TDA._textAttributes.getFirst()._bi);
            assertEquals(3, TDA._textAttributes.getFirst()._ei);

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getLast()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=plain,size=10]",
                    TDA._textAttributes.getLast()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_SUPERSCRIPT, TDA._textAttributes.getLast()._imposedOperation);
            assertEquals(4, TDA._textAttributes.getLast()._bi);
            assertEquals(5, TDA._textAttributes.getLast()._ei);

            assertEquals("\\\\$TE$S^T^", TDA._text);
            assertEquals("\\TEST", TDA._parsed);
        }

        //====
        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\$$TE^{ST}$", 10.0f, null);
            assertEquals(2, TDA._textAttributes.size());

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getFirst()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_ITALIC,
                    TDA._textAttributes.getFirst()._imposedOperation);
            assertEquals(1, TDA._textAttributes.getFirst()._bi);
            assertEquals(5, TDA._textAttributes.getFirst()._ei);

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getLast()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getLast()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_SUPERSCRIPT,
                    TDA._textAttributes.getLast()._imposedOperation);
            assertEquals(3, TDA._textAttributes.getLast()._bi);
            assertEquals(5, TDA._textAttributes.getLast()._ei);


            assertEquals("\\$$TE^{ST}$", TDA._text);
            assertEquals("$TEST", TDA._parsed);
        }
        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\$$TE^{S}T$", 10.0f, null);
            assertEquals(2, TDA._textAttributes.size());

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getFirst()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_ITALIC,
                    TDA._textAttributes.getFirst()._imposedOperation);
            assertEquals(1, TDA._textAttributes.getFirst()._bi);
            assertEquals(5, TDA._textAttributes.getFirst()._ei);

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getLast()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getLast()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_SUPERSCRIPT,
                    TDA._textAttributes.getLast()._imposedOperation);
            assertEquals(3, TDA._textAttributes.getLast()._bi);
            assertEquals(4, TDA._textAttributes.getLast()._ei);

            assertEquals("\\$$TE^{S}T$", TDA._text);
            assertEquals("$TEST", TDA._parsed);
        }
        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\$$TE^{}ST$", 10.0f, null);
            assertEquals(1, TDA._textAttributes.size());

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getFirst()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_ITALIC,
                    TDA._textAttributes.getFirst()._imposedOperation);
            assertEquals(1, TDA._textAttributes.getFirst()._bi);
            assertEquals(5, TDA._textAttributes.getFirst()._ei);

            assertEquals("\\$$TE^{}ST$", TDA._text);
            assertEquals("$TEST", TDA._parsed);
        }

        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\$$TE^{S\\\\}T$", 10.0f, null);
            assertEquals(2, TDA._textAttributes.size());

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getFirst()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_ITALIC,
                    TDA._textAttributes.getFirst()._imposedOperation);
            assertEquals(1, TDA._textAttributes.getFirst()._bi);
            assertEquals(6, TDA._textAttributes.getFirst()._ei);

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getLast()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getLast()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_SUPERSCRIPT,
                    TDA._textAttributes.getLast()._imposedOperation);
            assertEquals(3, TDA._textAttributes.getLast()._bi);
            assertEquals(5, TDA._textAttributes.getLast()._ei);

            assertEquals("\\$$TE^{S\\\\}T$", TDA._text);
            assertEquals("$TES\\T", TDA._parsed);
        }
        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\$$TE^{S\\}T$", 10.0f, null);
            assertEquals(0, TDA._textAttributes.size());
            assertEquals("\\$$TE^{S\\}T$", TDA._text);
            assertEquals("$TEST$", TDA._parsed);
        }


        // ===

        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("\\$$TE^{S\\\\}T _{TEST}$", 10.0f, null);
            assertEquals(3, TDA._textAttributes.size());

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getFirst()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_ITALIC,
                    TDA._textAttributes.getFirst()._imposedOperation);
            assertEquals(1, TDA._textAttributes.getFirst()._bi);
            assertEquals(11, TDA._textAttributes.getFirst()._ei);

            assertEquals(TextAttribute.FONT, TDA._textAttributes.get(1)._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.get(1)._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_SUPERSCRIPT,
                    TDA._textAttributes.get(1)._imposedOperation);
            assertEquals(3, TDA._textAttributes.get(1)._bi);
            assertEquals(5, TDA._textAttributes.get(1)._ei);

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getLast()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.getLast()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_SUBSCRIPT,
                    TDA._textAttributes.getLast()._imposedOperation);
            assertEquals(7, TDA._textAttributes.getLast()._bi);
            assertEquals(11, TDA._textAttributes.getLast()._ei);


            assertEquals("\\$$TE^{S\\\\}T _{TEST}$", TDA._text);
            assertEquals("$TES\\T TEST", TDA._parsed);
        }

        {
            FontProcessor font = new FontProcessor(new Font("Times New Roman", Font.PLAIN, 10));
            assertNull(font.parseTextDependentState(null, 10.0f, null));
            AbstractFontProcessor.TextDependentState TDA = font.parseTextDependentState("^TE$ST$ PL_OT", 10.0f, null);
            assertEquals(3, TDA._textAttributes.size());
            assertEquals("^TE$ST$ PL_OT", TDA._text);
            assertEquals("TEST PLOT", TDA._parsed);

            assertEquals(TextAttribute.FONT, TDA._textAttributes.get(1)._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=plain,size=10]",
                    TDA._textAttributes.getFirst()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_SUPERSCRIPT,
                    TDA._textAttributes.getFirst()._imposedOperation);
            assertEquals(0, TDA._textAttributes.getFirst()._bi);
            assertEquals(1, TDA._textAttributes.getFirst()._ei);

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getFirst()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=italic,size=10]",
                    TDA._textAttributes.get(1)._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_ITALIC,
                    TDA._textAttributes.get(1)._imposedOperation);
            assertEquals(2, TDA._textAttributes.get(1)._bi);
            assertEquals(4, TDA._textAttributes.get(1)._ei);

            assertEquals(TextAttribute.FONT, TDA._textAttributes.getLast()._attribute);
            assertEquals("java.awt.Font[family=Times New Roman,name=Times New Roman,style=plain,size=10]",
                    TDA._textAttributes.getLast()._value.toString());
            assertEquals(AbstractFontProcessor.IMPOSE_SUBSCRIPT,
                    TDA._textAttributes.getLast()._imposedOperation);
            assertEquals(7, TDA._textAttributes.getLast()._bi);
            assertEquals(8, TDA._textAttributes.getLast()._ei);
        }
    }
}