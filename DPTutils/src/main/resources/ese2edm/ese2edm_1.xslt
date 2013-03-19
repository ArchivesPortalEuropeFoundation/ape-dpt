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

    <!--Parameters, values coming from Java code-->
    <xsl:param name="oai_pmh_location"></xsl:param>

    <!-- template matching the root node and creating the RDF start tag -->
    <xsl:template match="/">
        <rdf:RDF>
            <xsl:apply-templates select="metadata/record"/>
        </rdf:RDF>
    </xsl:template>
        
    <!-- template matching a single ESE XML record -->
    <xsl:template match="metadata/record">            
        <!-- Generate structure tree of original EAD within skos:Concept objects -->
        <xsl:if test='dcterms:alternative'>
            <xsl:call-template name="generateSkos">
                <xsl:with-param name="inputLine" select="fn:substring-after(dcterms:alternative, ': ')" />
            </xsl:call-template>
        </xsl:if>
                
        <!-- Provided Cultural Heritage Object -->
        <edm:ProvidedCHO>
            <xsl:attribute name="rdf:about">
                <xsl:value-of select="dc:identifier"/>
            </xsl:attribute>
            <!-- deal with "other" corresponding properties -->
            <xsl:call-template name="mapChoProperties"/>
        </edm:ProvidedCHO>

        <!-- Web Resource information -->
        <xsl:call-template name="generateWebResource">
            <xsl:with-param name="objectUrl">
                <xsl:value-of select="europeana:isShownAt"/>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
        
    <!-- this template generates a hasPart property -->
    <xsl:template name="generatePart">
        <xsl:param name="inputLine"/>
        <xsl:variable name="id" select="fn:substring-before($inputLine,' ')"/>
        <dcterms:hasPart>
            <xsl:value-of select="$id"/>
        </dcterms:hasPart>
    </xsl:template>

    <!-- this template generates/edits a skos:Content instances -->
    <xsl:template name="generateSkos">
        <!--Parameter and variables-->
        <xsl:param name="inputLine"/>
        <xsl:variable name="levelId" select="fn:substring-before($inputLine,' ')"/>
        <xsl:variable name="levelTitle">
            <xsl:choose>
                <xsl:when test="fn:contains($inputLine, ' >> ')">
                    <xsl:value-of select='fn:substring-before(fn:substring-after($inputLine," ")," >> ")'></xsl:value-of>                             
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="fn:substring-after($inputLine,' ')"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="remainingLine" select='fn:substring-after($inputLine," >> ")'></xsl:variable>
        <!-- Actual content processing starts here -->
        <skos:Concept>
            <xsl:attribute name="rdf:about">
                <xsl:value-of select="$levelId"/>
            </xsl:attribute>
            <dc:title>
                <xsl:value-of select="$levelTitle"/>
            </dc:title>
            <xsl:choose>
                <xsl:when test='$remainingLine != ""'>
                    <xsl:call-template name="generatePart">
                        <xsl:with-param name="inputLine" select="$remainingLine"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <dcterms:hasPart>
                        <xsl:text></xsl:text>
                        <xsl:value-of select="dc:identifier"/>
                    </dcterms:hasPart>
                </xsl:otherwise>
            </xsl:choose>
        </skos:Concept>
        <xsl:if test='$remainingLine != ""'>
            <xsl:call-template name="generateSkos">
                <xsl:with-param name="inputLine" select="$remainingLine"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
		
    <!-- this template generates a edm:WebResource entry -->
    <xsl:template name="generateWebResource">
        <xsl:param name="objectUrl"/>
        <edm:WebResource>
            <xsl:attribute name="rdf:about">
                <xsl:value-of select="$objectUrl"/>
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
    </xsl:template>
        
    <!-- a named template, which can be called for mapping all other properties 
            TODO:
                    - improve this and simply match for previously unmatched nodes
                    - this could also be improved with XSLT 2.0 copy-of
    -->
    <xsl:template name="mapChoProperties">

        <xsl:for-each select="dc:contributor">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:contributor</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		
		
        <xsl:for-each select="dc:coverage">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:coverage</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>
		
        <xsl:for-each select="dc:creator">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:creator</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		
		
        <xsl:for-each select="dc:date">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:date</xsl:with-param>
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
		
        <xsl:for-each select="dc:identifier">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:identifier</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		
		
        <xsl:for-each select="dc:language">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:language</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		

        <xsl:for-each select="dc:publisher">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:publisher</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		

        <xsl:for-each select="dc:relation">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:relation</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		

        <xsl:for-each select="dc:subject">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:subject</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		

        <xsl:for-each select="dc:title">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:title</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>
		
        <xsl:for-each select="dc:type">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dc:type</xsl:with-param>
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
		
        <xsl:for-each select="dcterms:extent">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:extent</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>
		
        <xsl:for-each select="dcterms:hasFormat">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:hasFormat</xsl:with-param>
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

        <xsl:for-each select="dcterms:issued">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:issued</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		

        <xsl:for-each select="dcterms:medium">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:medium</xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>		
		
        <xsl:for-each select="dcterms:provenance">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:provenance</xsl:with-param>
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

        <xsl:for-each select="dcterms:spatial">
            <xsl:call-template name="create_property">
                <xsl:with-param name="tgt_property">dcterms:spatial</xsl:with-param>
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
		
        <xsl:for-each select="europeana:dataProvider">
            <edm:dataProvider>
                <xsl:value-of select="."/>
            </edm:dataProvider>
        </xsl:for-each>
                
        <xsl:for-each select="europeana:object">
            <edm:isShownAt>
                <xsl:value-of select="."/>
            </edm:isShownAt>
        </xsl:for-each>
                
        <xsl:for-each select="europeana:provider">
            <edm:provider>
                <xsl:value-of select="."/>
            </edm:provider>
        </xsl:for-each>
                
        <xsl:for-each select="europeana:type">
            <edm:type>
                <xsl:value-of select="."/>
            </edm:type>
        </xsl:for-each>		
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
