<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xmlns:edm="http://www.europeana.eu/schemas/edm/"
 xmlns:enrichment="http://www.europeana.eu/schemas/edm/enrichment"
 xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:wgs84="http://www.w3.org/2003/01/geo/wgs84_pos#"
 xmlns:skos="http://www.w3.org/2004/02/skos/core#"
 xmlns:ore="http://www.openarchives.org/ore/terms/" xmlns:oai="http://www.openarchives.org/OAI/2.0"
 xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:dcterms="http://purl.org/dc/terms/"
 xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:europeana="http://www.europeana.eu/schemas/ese/"
 xmlns="http://www.europeana.eu/schemas/edm/" xmlns:fn="http://www.w3.org/2005/xpath-functions"
 xmlns:xlink="http://www.w3.org/1999/xlink" exclude-result-prefixes="xlink fo fn">
 <xsl:output omit-xml-declaration="yes" indent="yes"/>
 <xsl:strip-space elements="*"/>

 <xsl:template match="node()|@*" name="identity">
  <xsl:copy>
   <xsl:apply-templates select="node()|@*"/>
  </xsl:copy>
 </xsl:template>
 
 <xsl:template match="rdf:RDF/ore:Aggregation">
  <xsl:variable name="curr" select="."/>
  <xsl:variable name="next" select="following-sibling::ore:Aggregation[1]"/>
  <record>
   <xsl:choose>
    <xsl:when test="$next">
     <xsl:sequence select=". | ./following-sibling::*[count(.|$next/preceding-sibling::*) = count($next/preceding-sibling::*)]"/>
    </xsl:when>
    <xsl:otherwise>
     <xsl:sequence select=". | ./following-sibling::node()"/>
    </xsl:otherwise>
   </xsl:choose>   
  </record>
 </xsl:template>
 
 <xsl:template match="rdf:RDF/*[self::edm:ProvidedCHO or self::edm:WebResource]"/>
</xsl:stylesheet>
