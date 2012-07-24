<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="urn:isbn:1-931666-22-9"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd"
                xpath-default-namespace="urn:isbn:1-931666-22-9"
                exclude-result-prefixes="xsl">

    <xsl:template name="frontmatter2scopecontent">
        <xsl:apply-templates select="/ead/frontmatter" mode="frontmatter"/>
    </xsl:template>

    <xsl:template match="frontmatter" mode="frontmatter">
        <scopecontent encodinganalog="summary">
            <xsl:apply-templates mode="frontmatter"/>
        </scopecontent>
    </xsl:template>

    <xsl:template match="div" mode="frontmatter">
        <p>
            <xsl:apply-templates mode="frontmatter"/>
        </p>
    </xsl:template>

    <xsl:template match="div/head" mode="frontmatter">
        <xsl:apply-templates mode="frontmatter"/>
    </xsl:template>

    <xsl:template match="div/p | div/p/note/p" mode="frontmatter">
        <xsl:apply-templates mode="frontmatter"/>
    </xsl:template>

    <xsl:template match="div/p/note" mode="frontmatter">
        <xsl:apply-templates mode="frontmatter"/>
    </xsl:template>

    <xsl:template match="div/p/list | div/p/list/item" mode="frontmatter">
        <xsl:apply-templates mode="frontmatter"/>
    </xsl:template>

</xsl:stylesheet>