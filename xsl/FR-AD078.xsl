<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="urn:isbn:1-931666-22-9"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:none="none"
                xmlns:ape="http://www.archivesportaleurope.net/functions"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd"
                xpath-default-namespace="urn:isbn:1-931666-22-9" exclude-result-prefixes="xsl fo xs none ape">

    <xsl:import href="system/default-apeEAD.xsl"/>

    <xsl:template match="otherfindaid/p/extref" mode="copy level">
        <extref>
            <xsl:attribute name="xlink:href">
                <xsl:value-of select="replace(@href, '.xml', '')" />
            </xsl:attribute>
            <xsl:attribute name="xlink:title" select="@title"/>
        </extref>
    </xsl:template>
    <xsl:template match="bibliography/p[@altrender='sous-titre_01']" mode="copy level">
        <p><emph render="bold">
            <xsl:value-of select="text()" />
        </emph></p>
    </xsl:template>

</xsl:stylesheet>