<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:dc="http://purl.org/dc/elements/1.1/"
                xmlns:dcterms="http://purl.org/dc/terms/"
                xmlns:edm="http://www.europeana.eu/schemas/edm/"
                xmlns:enrichment="http://www.europeana.eu/schemas/edm/enrichment"
                xmlns:europeana="http://www.europeana.eu/schemas/ese/"
                xmlns:oai="http://www.openarchives.org/OAI/2.0/"
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

<xsl:key name="skosId" match="skos:Concept" use="@rdf:about" />
<xsl:key name="kHasName" match="dcterms:hasPart" use="../@rdf:about" />

    <xsl:template match="node()|@*">
      <xsl:copy>
        <xsl:apply-templates select="node()|@*"/>
      </xsl:copy>
    </xsl:template>
    
    <xsl:template match="skosConcept[generate-id() = generate-id(key('skosId', @rdfAbout)[1])]">
        <xsl:if test="generate-id() = generate-id(key('skosId', @rdfAbout)[1])">
            <xsl:copy>
                <xsl:apply-templates select="@* | key('skosId', @rdfAbout)/node" />
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="skosConcept" />
    
</xsl:stylesheet>
