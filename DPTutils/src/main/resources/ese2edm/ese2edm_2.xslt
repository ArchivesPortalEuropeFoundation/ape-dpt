<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:dc="http://purl.org/dc/elements/1.1/"
                xmlns:dcterms="http://purl.org/dc/terms/"
                xmlns:edm="http://www.europeana.eu/schemas/edm/"
                xmlns:enrichment="http://www.europeana.eu/schemas/edm/enrichment"
                xmlns:europeana="http://www.europeana.eu/schemas/ese/"
                xmlns:ore="http://www.openarchives.org/ore/terms/"
                xmlns:owl="http://www.w3.org/2002/07/owl#"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:skos="http://www.w3.org/2004/02/skos/core#"
                xmlns:wgs84="http://www.w3.org/2003/01/geo/wgs84_pos#"
                xpath-default-namespace="http://www.europeana.eu/schemas/ese/"
                xmlns:fn="http://www.w3.org/2005/xpath-functions"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                exclude-result-prefixes="xlink fn"
                version="2.0">
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>

    <!--Parameters, values coming from Java code-->
    <xsl:param name="edm_identifier"></xsl:param>

    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>

    <!--<xsl:template match="skos:Concept[position() = 1]" priority="3">-->
    <xsl:template match="rdf:RDF">
        <rdf:RDF xsi:schemaLocation="http://purl.org/dc/elements/1.1/ http://dublincore.org/schemas/xmls/qdc/dc.xsd
        http://purl.org/dc/terms/ http://dublincore.org/schemas/xmls/qdc/dcterms.xsd
        http://www.europeana.eu/schemas/edm/ http://www.europeana.eu/schemas/edm/EDM-EXTERNAL-MAIN.xsd
        http://www.europeana.eu/schemas/edm/enrichment http://www.europeana.eu/schemas/edm/ENRICHMENT.xsd
        http://www.europeana.eu/schemas/ese/ http://www.europeana.eu/schemas/ese/ESE-V3.4.xsd
        http://www.openarchives.org/ore/terms/ http://www.europeana.eu/schemas/edm/ORE.xsd
        http://www.w3.org/2002/07/owl# http://www.europeana.eu/schemas/edm/OWL.xsd
        http://www.w3.org/1999/02/22-rdf-syntax-ns# http://www.europeana.eu/schemas/edm/RDF.xsd
        http://www.w3.org/2004/02/skos/core# http://www.europeana.eu/schemas/edm/SKOS.xsd
        http://www.w3.org/2003/01/geo/wgs84_pos# http://www.europeana.eu/schemas/edm/WGS84.xsd">
            <xsl:for-each-group select="./skos:Concept" group-by="@rdf:about">
                <skos:Concept>
                    <xsl:attribute name="rdf:about" select="current-group()[1]/@rdf:about"/>
                    <xsl:copy-of select="current-group()[1]/dc:title"/>
                    <xsl:for-each-group select="current-group()" group-by="if(dcterms:hasPart/text()) then dcterms:hasPart/text() else 'nothing'">
                        <xsl:copy-of select="current-group()[1]/dcterms:hasPart" />
                    </xsl:for-each-group>
                </skos:Concept>
            </xsl:for-each-group>
            <xsl:apply-templates />
        </rdf:RDF>
    </xsl:template>

    <xsl:template match="edm:ProvidedCHO">
        <edm:ProvidedCHO>
            <xsl:copy-of select="./*"/>
            <xsl:variable name="identifier" select="dc:identifier"/>
            <xsl:for-each-group select="/rdf:RDF/skos:Concept" group-by="@rdf:about">
                <xsl:for-each-group select="current-group()" group-by="if(dcterms:hasPart/text()) then dcterms:hasPart/text() else 'nothing'">
                    <xsl:if test="current-group()[1]/dcterms:hasPart = $identifier">
                        <dcterms:isPartOf>
                            <xsl:value-of select="current-group()[1]/@rdf:about" />
                        </dcterms:isPartOf>
                    </xsl:if>
                </xsl:for-each-group>
            </xsl:for-each-group>
        </edm:ProvidedCHO>
    </xsl:template>
    
    <xsl:template match="edm:WebResource">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <xsl:template match="skos:Concept"/>

    <!--
    1) generate result tree
    2) check each skos:Concept for hasPart-values not beginning with eadid
    3) if more than 2 such values exist in the respective skos:Concept, add
       <dcterms:isNextInSequence>Value2</dcterms:isNExtInSequence>
       to respective edm:ProvidedCHO
    -->
    
</xsl:stylesheet>
