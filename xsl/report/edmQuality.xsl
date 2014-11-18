<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="urn:isbn:1-931666-22-9"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:edm="http://www.europeana.eu/schemas/edm/"
                xmlns:enrichment="http://www.europeana.eu/schemas/edm/enrichment"
                xmlns:owl="http://www.w3.org/2002/07/owl#"
                xmlns:wgs84="http://www.w3.org/2003/01/geo/wgs84_pos#"
                xmlns:skos="http://www.w3.org/2004/02/skos/core#"
                xmlns:ore="http://www.openarchives.org/ore/terms/"
                xmlns:oai="http://www.openarchives.org/OAI/2.0"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:dcterms="http://purl.org/dc/terms/" xmlns:dc="http://purl.org/dc/elements/1.1/"
                xmlns:europeana="http://www.europeana.eu/schemas/ese/"
                xmlns:ape="http://www.archivesportaleurope.net/functions"
                xmlns:saxon="http://saxon.sf.net/"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xsi:schemaLocation="http://www.w3.org/1999/02/22-rdf-syntax-ns# http://www.europeana.eu/schemas/edm/EDM.xsd"
                xpath-default-namespace="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                exclude-result-prefixes="xsl xs ape saxon">

    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="edm:ProvidedCHO">
        <xsl:value-of select="ape:checkIfExist(.)"/>
    </xsl:template>

</xsl:stylesheet>
