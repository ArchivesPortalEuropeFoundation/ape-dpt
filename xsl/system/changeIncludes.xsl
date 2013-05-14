<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:param name="mainPath" select="'/'" />

    <xsl:template match="xsl:include">
        <xsl:variable name="filePath"><xsl:value-of select="$mainPath"/><xsl:value-of select="@href"/></xsl:variable>
        <xsl:copy-of select="document($filePath)/xsl:stylesheet/*"/>
    </xsl:template>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>