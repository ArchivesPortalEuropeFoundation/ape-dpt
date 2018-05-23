<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : ead3conversion.xsl
    Created on : May 17, 2018, 4:14 PM
    Author     : kaisar
    Description:
        All the C* element has to be transformed to C.
        
-->

<xsl:stylesheet version="2.0" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns='http://www.w3.org/1999/xhtml' xmlns:ead="http://ead3.archivists.org/schema/" xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:ape="http://www.archivesportaleurope.eu/xslt/extensions"
                exclude-result-prefixes="xlink xlink xsi ead ape fn #default">

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="ead:c01|ead:c02|ead:c03|ead:c04|ead:c05|ead:c06|ead:c07|ead:c08|ead:c09|ead:c10|ead:c11|ead:c12">
        <c>
            <xsl:apply-templates select="@*|node()" />
        </c>
    </xsl:template>

</xsl:stylesheet>
