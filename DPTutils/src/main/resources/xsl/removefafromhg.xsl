<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0"
            xmlns="urn:isbn:1-931666-22-9"
            xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
            xmlns:xlink="http://www.w3.org/1999/xlink"
            xmlns:ape="http://www.archivesportaleurope.net/functions" exclude-result-prefixes="#all">

            <xsl:param name="cident"/>
            <xsl:output indent="yes" method="xml" omit-xml-declaration="yes"/>

            <xsl:template match="node() | @*">
                        <xsl:copy>
                                    <xsl:apply-templates select="node() | @*"/>
                        </xsl:copy>
            </xsl:template>

            <xsl:template match="c">
                        <xsl:choose>
                                    <xsl:when test="otherfindaid/p/extref/@*:href = $cident"/>
                                    <xsl:otherwise>
                                                <xsl:copy>
                                                  <xsl:apply-templates select="node() | @*"/>
                                                </xsl:copy>
                                    </xsl:otherwise>
                        </xsl:choose>
            </xsl:template>
</xsl:stylesheet>
