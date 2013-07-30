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
    <xsl:param name="prefix_url"></xsl:param>
    <xsl:param name="repository_code"></xsl:param>
    <xsl:param name="xml_type_name"></xsl:param>
    
    <!--variable for identifying the eadid, stored at /metadata/record[1]/dc:identifier-->
    <xsl:variable name="eadid" select="/metadata/record[1]/dc:identifier[1]" />
    <!--variable for base path of document identifiers-->
    <xsl:variable name="id_base" select="concat($prefix_url, '/' , $repository_code, '/', $xml_type_name, '/', substring-after($eadid, '_'))" />
    
    <!-- template matching the root node and creating the RDF start tag -->
    <xsl:template match="/">
        <rdf:RDF xsi:schemaLocation="http://www.w3.org/1999/02/22-rdf-syntax-ns# http://www.europeana.eu/schemas/edm/EDM.xsd">
            <xsl:apply-templates select="metadata/record"/>
        </rdf:RDF>
    </xsl:template>
        
    <!-- template matching a single ESE XML record -->
    <xsl:template match="metadata/record">            
        
        <!-- Provider aggregation -->
        <ore:Aggregation>
            <xsl:attribute name="rdf:about">
                <xsl:value-of select="concat('aggregation_', dc:identifier[1])"/>
            </xsl:attribute>
            <edm:aggregatedCHO>
                <xsl:attribute name="rdf:resource">
                    <xsl:value-of select="concat('providedCHO_', dc:identifier[1])"/>
                </xsl:attribute>
            </edm:aggregatedCHO>
            <xsl:for-each select='europeana:dataProvider'>
                <edm:dataProvider>
                    <xsl:value-of select="."/>
                </edm:dataProvider>
            </xsl:for-each>
            <xsl:for-each select="europeana:object">
                <edm:isShownBy>
                    <xsl:attribute name="rdf:resource">
                        <xsl:value-of select="."/>
                    </xsl:attribute>
                </edm:isShownBy>
            </xsl:for-each>
            <edm:isShownAt>
                <xsl:attribute name="rdf:resource">
                    <xsl:choose>
                        <xsl:when test='string-length(europeana:isShownAt) > 0'>
                            <xsl:value-of select="europeana:isShownAt"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:if test='position() = 1'>
                                <xsl:value-of select="concat($prefix_url, '/', $repository_code, '/', $xml_type_name, '/', substring-after($eadid, '_'))"/>
                            </xsl:if>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute>
            </edm:isShownAt>
            <!--            <xsl:choose>
                <xsl:when test='position() = 1'>
                    <xsl:for-each select="europeana:isShownAt">
                        <edm:isShownAt>
                            <xsl:attribute name="rdf:resource">
                                <xsl:value-of select="$id_base"/>
                            </xsl:attribute>
                        </edm:isShownAt>
                    </xsl:for-each>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:for-each select="europeana:isShownAt">
                        <edm:isShownAt>
                            <xsl:attribute name="rdf:resource">
                                <xsl:value-of select="."/>
                            </xsl:attribute>
                        </edm:isShownAt>
                    </xsl:for-each>
                </xsl:otherwise>
            </xsl:choose>-->
            <xsl:for-each select="europeana:provider">
                <edm:provider>
                    <xsl:value-of select="."/>
                </edm:provider>
            </xsl:for-each>
            <xsl:for-each select="dc:rights">
                <dc:rights>
                    <xsl:value-of select="."/>
                </dc:rights>
            </xsl:for-each>
            <xsl:for-each select="europeana:rights">
                <edm:rights>
                    <xsl:attribute name="rdf:resource">
                        <xsl:value-of select="."/>
                    </xsl:attribute>
                </edm:rights>
            </xsl:for-each>
        </ore:Aggregation>
    
        <!-- Provided Cultural Heritage Object -->
        <edm:ProvidedCHO>
            <xsl:attribute name="rdf:about">
                <xsl:value-of select="concat('providedCHO_', dc:identifier[1])"/>
            </xsl:attribute>
            <dc:identifier>
                <xsl:choose>
                    <xsl:when test='position() = 1'>
                        <xsl:value-of select="dc:identifier[2]"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="dc:identifier[1]"/>
                    </xsl:otherwise>
                </xsl:choose>
            </dc:identifier>
            <!-- deal with "other" corresponding properties -->
            <xsl:call-template name="mapChoProperties"/>
            <xsl:if test='position() > 1'>
                <dcterms:isPartOf>
                    <xsl:value-of select="concat('providedCHO_', $eadid)"/>
                </dcterms:isPartOf>
            </xsl:if>
            <xsl:if test='preceding-sibling::record[1]/dcterms:alternative eq dcterms:alternative'>
                <edm:isNextInSequence>
                    <xsl:attribute name="rdf:resource">
                        <xsl:value-of select="concat('providedCHO_', preceding-sibling::record[1]/dc:identifier[1])" />
                    </xsl:attribute>
                </edm:isNextInSequence>
            </xsl:if>
            <dc:subject>
                <xsl:if test='position() > 1'>
                    <xsl:attribute name="rdf:resource">
                        <xsl:value-of select="concat('context_', dc:identifier[1])"/>
                    </xsl:attribute>
                </xsl:if>
                <xsl:if test='position() = 1'>
                    <xsl:text>Finding aid</xsl:text>
                </xsl:if>
            </dc:subject>

        </edm:ProvidedCHO>

        <!-- Web Resource information -->
        <edm:WebResource>
            <xsl:attribute name="rdf:about">
                <xsl:choose>
                    <xsl:when test='string-length(europeana:isShownAt) > 0'>
                        <xsl:value-of select="europeana:isShownAt"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:if test='position() = 1'>
                            <xsl:value-of select="concat($prefix_url, '/', $repository_code, '/', $xml_type_name, '/', substring-after($eadid, '_'))"/>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <xsl:for-each select="dc:rights">
                <dc:rights>
                    <xsl:attribute name="rdf:resource">
                        <xsl:value-of select="."/>
                    </xsl:attribute>
                </dc:rights>
            </xsl:for-each>
            <xsl:for-each select="europeana:rights">
                <edm:rights>
                    <xsl:attribute name="rdf:resource">
                        <xsl:value-of select="."/>
                    </xsl:attribute>
                </edm:rights>
            </xsl:for-each>
        </edm:WebResource>
        <xsl:if test='europeana:object'>
            <edm:WebResource>
                <xsl:attribute name="rdf:about">
                    <xsl:if test='string-length(europeana:object) > 0'>
                        <xsl:value-of select="europeana:object"/>
                    </xsl:if>
                </xsl:attribute>
                <xsl:for-each select="dc:rights">
                    <dc:rights>
                        <xsl:attribute name="rdf:resource">
                            <xsl:value-of select="."/>
                        </xsl:attribute>
                    </dc:rights>
                </xsl:for-each>
                <xsl:for-each select="europeana:rights">
                    <edm:rights>
                        <xsl:attribute name="rdf:resource">
                            <xsl:value-of select="."/>
                        </xsl:attribute>
                    </edm:rights>
                </xsl:for-each>
            </edm:WebResource>
        </xsl:if>
    
        <!-- Simple Knowledge Organization System -> Concept -->
        <xsl:if test='dcterms:alternative'>
            <skos:Concept>
                <xsl:attribute name="rdf:about">
                    <xsl:value-of select="concat('context_', dc:identifier[1])"/>
                </xsl:attribute>
                <xsl:for-each select="dcterms:alternative">
                    <skos:prefLabel>
                        <xsl:value-of select="."/>
                    </skos:prefLabel>
                </xsl:for-each>
            </skos:Concept>
        </xsl:if>
            
    </xsl:template>
        
    <!-- a named template, which can be called for mapping all other properties 
            TODO:
                    - improve this and simply match for previously unmatched nodes
                    - this could also be improved with XSLT 2.0 copy-of
    -->
    <xsl:template name="mapChoProperties">

        <!--<xsl:for-each select="dc:identifier">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:identifier</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		
        -->	
        <xsl:for-each select="dc:publisher">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:publisher</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		

        <xsl:for-each select="dcterms:issued">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:issued</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		

        <xsl:for-each select="dc:title">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:title</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>
		
        <xsl:for-each select="dc:description">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:description</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>
		
        <xsl:for-each select="dc:format">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:format</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>
		
        <xsl:for-each select="dcterms:extent">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:extent</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>
		
        <xsl:for-each select="dc:relation">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:relation</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		

        <xsl:for-each select="dc:creator">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:creator</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		
		
        <xsl:for-each select="dcterms:provenance">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:provenance</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		

        <xsl:for-each select="dc:date">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:date</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		
		
        <xsl:for-each select="dcterms:hasFormat">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:hasFormat</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		

        <xsl:for-each select="dc:coverage">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:coverage</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>
		
        <xsl:for-each select="dcterms:spatial">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:spatial</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>
		
        <xsl:for-each select="dc:subject">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:subject</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		

        <xsl:for-each select="dc:language">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:language</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		

        <!--<xsl:for-each select="dc:type">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:type</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>-->
        <!-- ================================================================================
           ITEMS BELOW THIS LINE ARE NOT USED IN apeESE, BUT KEPT FOR POSSIBLE FUTURE CHANGES
        ================================================================================= -->
        <xsl:for-each select="dc:contributor">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:contributor</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		
		
        <xsl:for-each select="dcterms:conformsTo">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:conformsTo</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		

        <xsl:for-each select="dcterms:created">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:created</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		
		
        <xsl:for-each select="dcterms:hasPart">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:hasPart</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>

        <xsl:for-each select="dcterms:hasVersion">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:hasVersion</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>

        <xsl:for-each select="dcterms:isFormatOf">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:isFormatOf</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>

        <xsl:for-each select="dcterms:isPartOf">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:isPartOf</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		
		
        <xsl:for-each select="dcterms:isReferencedBy">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:isReferencedBy</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>

        <xsl:for-each select="dcterms:isReplacedBy">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:isReplacedBy</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>

        <xsl:for-each select="dcterms:isRequiredBy">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:isRequiredBy</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		

        <xsl:for-each select="dcterms:isVersionOf">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:isVersionOf</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>

        <xsl:for-each select="dcterms:medium">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:medium</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		
		
        <xsl:for-each select="dcterms:references">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:references</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>

        <xsl:for-each select="dcterms:replaces">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:replaces</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>

        <xsl:for-each select="dcterms:requires">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:requires</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		

        <xsl:for-each select="dcterms:tableOfContents">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:tableOfContents</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>
                
        <xsl:for-each select="dcterms:temporal">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:temporal</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		
		
        <xsl:for-each select="europeana:type">
            <edm:type>
                <xsl:value-of select="."/>
            </edm:type>
        </xsl:for-each>		

        <!--<xsl:for-each select="europeana:unstored">
            <edm:unstored>
                <xsl:value-of select="."/>
            </edm:unstored>
        </xsl:for-each>-->
    </xsl:template>
	
	
    <!-- this template creates an output property with a given name and copies all attributes from the context node -->
    <xsl:template name="create_property">
        <xsl:param name="tgt_property"/>
        <xsl:element name="{$tgt_property}">
            <xsl:for-each select="@xml:lang">
                <xsl:copy/>
            </xsl:for-each>
            <xsl:value-of select="."/>
        </xsl:element>
    </xsl:template>
	
</xsl:stylesheet>
