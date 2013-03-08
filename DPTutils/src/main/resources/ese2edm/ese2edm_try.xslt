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
                xsi:schemaLocation="http://www.w3.org/1999/02/22-rdf-syntax-ns# EDM.xsd"
                xmlns:fn="http://www.w3.org/2005/xpath-functions"
                exclude-result-prefixes="fn">
    <xsl:output method="xml" indent="yes" />
    <!-- Root node-->
    <xsl:template match="/">
        <rdf:RDF>
            <xsl:call-template name="record">
                <xsl:with-param name="recordnode" select="/metadata/record"/>
            </xsl:call-template>
        </rdf:RDF>
    </xsl:template>
    
    <!-- metadata/record, aka all records -->
    <xsl:template name="record">
        <xsl:param name="recordnode"/>
        <xsl:call-template name="providedCHO"> 
            <xsl:with-param name="recordnode" select="$recordnode"/>
        </xsl:call-template>
        <xsl:call-template name="webResource"> 
            <xsl:with-param name="recordnode" select="$recordnode"/>
        </xsl:call-template>
        <xsl:call-template name="aggregation"> 
            <xsl:with-param name="recordnode" select="$recordnode"/>
        </xsl:call-template>
    </xsl:template>
    
    <!-- sub-templates for the respective EDM parts -->
    <xsl:template name="providedCHO">
        <xsl:param name="recordnode"/>
        <edm:providedCHO>
            <jedi>jedi1</jedi>
            <xsl:value-of select="fn:name()"/>
            <jedi>jedi2</jedi>
            <xsl:value-of select="fn:local-name()"/>
            <jedi>jedi3</jedi>
            <xsl:value-of select="fn:namespace-uri()"/>
            <jedi>jedi4</jedi>
            <xsl:value-of select="fn:name(fn:root())"/>
            <jedi>jedi5</jedi>
            <xsl:for-each select='identifier'>
                <yada>yada</yada>
            </xsl:for-each>
        </edm:providedCHO>
    </xsl:template>

    <xsl:template name="webResource">
        <xsl:param name="recordnode"/>
        <edm:webResource>
        </edm:webResource>    
    </xsl:template>

    <xsl:template name="aggregation">
        <xsl:param name="recordnode"/>
        <ore:aggregation>
        </ore:aggregation>
    </xsl:template>
    
    <!-- templates used within metadata/record -->
    <xsl:template name="dc:identifier">
        <dc:identifier>
            <xsl:value-of select="."/>
        </dc:identifier>
    </xsl:template>
    
</xsl:stylesheet>