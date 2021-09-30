<xsl:stylesheet version="2.0" xmlns="urn:isbn:1-931666-22-9"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:none="none"
    xmlns:ape="http://www.archivesportaleurope.net/functions"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd"
    xpath-default-namespace="urn:isbn:1-931666-22-9" exclude-result-prefixes="xsl fo xs none ape">

    <xsl:import href="system/default-apeEAD.xsl"/>

<!--    enhance all unitid elements with information from the processinfo element-->
    <xsl:template match="c[./processinfo]/did/unitid" mode="#all">
        <xsl:element name="unitid" exclude-result-prefixes="#all">
            <xsl:attribute name="type" select="'call number'"/>
            <xsl:value-of select="./text()"/>
            <xsl:copy-of select="../../processinfo/p/extptr" copy-namespaces="no"/>
        </xsl:element>
    </xsl:template>

    <!-- remove information under processinfo elements-->
    <xsl:template match="processinfo" mode="#all"/>



</xsl:stylesheet>
