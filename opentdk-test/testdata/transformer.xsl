<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <html>
            <head>
                <link rel="stylesheet" type="text/css" href="style.css"/>
                <script src="jQuery.js"/>
                <script type="text/javascript">
                    $(document).ready(function (){
                        $('.stepBegin').click(function (){
                            $(this).toggleClass('expand').nextUntil('.stepBegin').slideToggle(100);
                        });
                    })
                </script>
            </head>
            <body>
                <h2>REPORT SUMMARY</h2>
                <h3>
                    <xsl:value-of select="test/@testName"/>
                </h3>

                <table>

                    <tr>
                        <th>Step name</th>
                        <th>Regel</th>
                        <th>Operation</th>
                        <th>Object Name</th>
                        <th>Status</th>
                        <th>Werte</th>
                        <th>Eigenschaft</th>
                        <th>Soll</th>
                        <th>Ist</th>
                        <th>...</th>
                    </tr>

                        <xsl:for-each select="//instruction">
                                <xsl:if test="not(StepName = preceding-sibling::*[1]/StepName)">
                                    <tr>
                                        <xsl:attribute name="class">stepBegin</xsl:attribute>
                                        <th colspan="10" style="text-align:left">

                                            <xsl:value-of select="StepName"/>
                                            <span>
                                                <xsl:attribute name="class">sign</xsl:attribute>
                                                <xsl:text> </xsl:text>
                                            </span>
                                        </th>

                                        <xsl:if test="not('Passed' = preceding-sibling::*[1]/Status)">
                                            <xsl:attribute name="status">Failed</xsl:attribute>
                                        </xsl:if>
                                    </tr>
                                </xsl:if>
                            <tr>
                                <xsl:if test="Status = 'Ignored'">
                                    <xsl:attribute name="status">Ignored</xsl:attribute>
                                </xsl:if>
                                <xsl:if test="Status = 'Failed'">
                                        <xsl:attribute name="status">Failed</xsl:attribute>
                                </xsl:if>

                                <td/>
                                <td><xsl:value-of select="Notation"/></td>
                                <td><xsl:value-of select="Operation"/></td>
                                <td><xsl:value-of select="ObjectName"/></td>
                                <td><xsl:value-of select="Status"/></td>
                                <td><xsl:value-of select="Value"/></td>
                                <td><xsl:value-of select="PropertyName"/></td>
                                <td><xsl:value-of select="PropertyValue"/></td>
                                <xsl:if test="Status = 'Passed'">
                                    <td/>
                                    <td/>
                                </xsl:if>
                                <xsl:if test="Status = 'Ignored'">
                                    <td><xsl:value-of select="Actual"/></td>
                                    <td/>
                                </xsl:if>

                                <xsl:if test="Status = 'Failed'">
                                    <td><xsl:value-of select="Actual"/></td>
                                    <td>
                                        <xsl:variable name="screenshot">
                                            <xsl:value-of select="StepID"/>
                                        </xsl:variable>
                                        <a>
                                            <xsl:attribute name="href">
                                                <xsl:value-of select="concat($screenshot,'.png')"/>
                                            </xsl:attribute>
                                            <img height="70" width="100" >
                                                <xsl:attribute name="src">
                                                    <xsl:value-of select="concat($screenshot,'.png')"/>
                                                </xsl:attribute>
                                            </img>
                                        </a>
                                    </td>
                                </xsl:if>
                            </tr>
                        </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>

