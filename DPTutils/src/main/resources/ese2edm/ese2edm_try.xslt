<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:dcterms="http://purl.org/dc/terms/"
                xmlns:edm="http://www.europeana.eu/schemas/edm/"
                xmlns:enrichment="http://www.europeana.eu/schemas/edm/enrichment/"
                xmlns:owl="http://www.w3.org/2002/07/owl#"
                xmlns:wgs84="http://www.w3.org/2003/01/geo/wgs84_pos#"
                xmlns:skos="http://www.w3.org/2004/02/skos/core#"
                xmlns:oai="http://www.openarchives.org/OAI/2.0/"
                xmlns:ore="http://www.openarchives.org/ore/terms/"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:dc="http://purl.org/dc/elements/1.1/"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xsi:schemaLocation="http://www.w3.org/1999/02/22-rdf-syntax-ns#EDM.xsd"
                xpath-default-namespace="urn:isbn:1-931666-22-9"
                xmlns:fn="http://www.w3.org/2005/xpath-functions"
                exclude-result-prefixes="fn">
<xsl:output method="xml" encoding="UTF-8" indent="yes" />

<xsl:template match="/">
    <rdf:RDF>
        <xsl:copy-of select="/"/>
    </rdf:RDF>
    <rdf:RDF>
        <xsl:copy-of select="."/>
    </rdf:RDF>
    <rdf:RDF>
        <xsl:copy-of select="metadata"/>
    </rdf:RDF>
    <xsl:for-each select="./child::node()/child::node()/child::node()">
        <xsl:element name="myElement">
            <xsl:value-of select="name()"/>
        </xsl:element>
    </xsl:for-each>
    <rdf:RDF>
        <xsl:apply-templates select="//record"/>
    </rdf:RDF>
</xsl:template>
    
<xsl:template match="record">
    <edm:providedCHO>
        <xsl:apply-templates select="dc:identifier"/>
        <xsl:apply-templates select="dc:publisher"/>
    </edm:providedCHO>
    <xsl:call-template name="providedCHO"> 
    </xsl:call-template>
    <xsl:call-template name="webResource"> 
    </xsl:call-template>
    <xsl:call-template name="aggregation"> 
    </xsl:call-template>
</xsl:template>
    
<xsl:template name="providedCHO">
    <edm:providedCHO>
        <xsl:apply-templates select="dc:identifier"/>
        <xsl:apply-templates select="dc:publisher"/>
    </edm:providedCHO>
</xsl:template>
<xsl:template name="webResource">
    <edm:webResource>
    </edm:webResource>
</xsl:template>
<xsl:template name="aggregation">
    <ore:aggregation>
    </ore:aggregation>
</xsl:template>

<xsl:template match="dc:identifier">
    <dc:identifier>
        <xsl:value-of select="."/>
    </dc:identifier>
</xsl:template>
<xsl:template match="dc:publisher">
    <dc:publisher>
        <xsl:value-of select="."/>
    </dc:publisher>
</xsl:template>
    <!--
    
    <xsl:template match="*[not(*)]">
        <xsl:value-of select="local-name()" />
        <xsl:text> </xsl:text>
        <xsl:value-of select="normalize-space(.)" />
        <xsl:text>&#10;&#13;</xsl:text>
    </xsl:template>-->
    
</xsl:stylesheet>
