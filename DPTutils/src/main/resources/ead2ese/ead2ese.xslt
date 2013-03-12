<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:europeana="http://www.europeana.eu/schemas/ese/" xmlns:dcterms="http://purl.org/dc/terms/"  xmlns="http://www.europeana.eu/schemas/ese/" xpath-default-namespace="urn:isbn:1-931666-22-9" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xlink="http://www.w3.org/1999/xlink" exclude-result-prefixes="xlink fn" >
<xsl:output method="xml" indent="yes" />  
<xsl:param name="europeana_provider"></xsl:param>
<xsl:param name="europeana_dataprovider"></xsl:param>
<xsl:param name="europeana_rights"></xsl:param>
<xsl:param name="dc_rights"></xsl:param>
<xsl:param name="europeana_type"></xsl:param>
<xsl:param name="useISODates"></xsl:param>
<xsl:param name="language"></xsl:param>
<xsl:param name="inheritElementsFromFileLevel"></xsl:param>
<xsl:param name="inheritOrigination"></xsl:param>
<xsl:param name="inheritLanguage"></xsl:param>
<xsl:param name="inheritCustodhist"></xsl:param>
<xsl:param name="inheritAltformavailHead"></xsl:param>
<xsl:param name="inheritControlaccess"></xsl:param>
<xsl:param name="contextInformationPrefix"></xsl:param>
<xsl:template match="/">
<metadata xsi:schemaLocation="http://www.europeana.eu/schemas/ese/ http://www.europeana.eu/schemas/ese/ESE-V3.4.xsd
http://purl.org/dc/elements/1.1/ http://www.dublincore.org/schemas/xmls/qdc/dc.xsd
http://purl.org/dc/terms/ http://www.dublincore.org/schemas/xmls/qdc/dcterms.xsd">
  <xsl:apply-templates select="/ead/archdesc"/>  
</metadata>
</xsl:template>
<xsl:template match="archdesc">
  <xsl:apply-templates select="dsc">
   	<xsl:with-param name="inheritedOriginations">
                    <xsl:if test="./did/origination">
                        <xsl:call-template name="creator">
                            <xsl:with-param name="originations" select="./did/origination"></xsl:with-param>
                        </xsl:call-template>
                    </xsl:if>
   	</xsl:with-param>
   	<xsl:with-param name="inheritedLanguages">
   		<xsl:if test="./did/langmaterial">
   			<xsl:call-template name="language">
				<xsl:with-param name="langmaterials" select="./did/langmaterial"></xsl:with-param>
			</xsl:call-template>	
   		</xsl:if>    	
   	</xsl:with-param>
   	<xsl:with-param name="inheritedCustodhists">
   		<xsl:if test="./custodhist">
   			<xsl:call-template name="custodhist">
                            <xsl:with-param name="custodhists" select="./custodhist"></xsl:with-param>
			</xsl:call-template>	
   		</xsl:if>    	
   	</xsl:with-param>
   	<xsl:with-param name="inheritedAltformavailHead">
   		<xsl:if test="./altformavail/head">
                    <xsl:call-template name="altformavailHead">
                        <xsl:with-param name="altformavailHeads" select="./altformavail/head"></xsl:with-param>
                    </xsl:call-template>
   		</xsl:if>    	
   	</xsl:with-param>
   	<xsl:with-param name="inheritedControlaccesses">
   		<xsl:if test="./controlaccess">
   			<xsl:call-template name="controlaccess">
				<xsl:with-param name="controlaccesses" select="./controlaccess"></xsl:with-param>
			</xsl:call-template>
   		</xsl:if>    	
   	</xsl:with-param>
  </xsl:apply-templates>  
</xsl:template>
<xsl:template match="dsc">
	<xsl:param name="inheritedOriginations"/>
	<xsl:param name="inheritedLanguages"/>
        <xsl:param name="inheritedCustodhists"/>
        <xsl:param name="inheritedAltformavailHeads"/>
        <xsl:param name="inheritedControlaccesses"/>
        <xsl:apply-templates select="c">
		<xsl:with-param name="inheritedOriginations" select="$inheritedOriginations"/>
	   	<xsl:with-param name="inheritedLanguages" select="$inheritedLanguages"/>
                <xsl:with-param name="inheritedCustodhists" select="$inheritedCustodhists"/>
                <xsl:with-param name="inheritedAltformavailHeads" select="$inheritedAltformavailHeads"/>
                <xsl:with-param name="inheritedControlaccesses" select="$inheritedControlaccesses"/>
  	</xsl:apply-templates>
</xsl:template>
<xsl:template match="c">
	<xsl:param name="inheritedOriginations"/>
	<xsl:param name="inheritedLanguages"/>
        <xsl:param name="inheritedCustodhists"/>
        <xsl:param name="inheritedAltformavailHeads"/>
        <xsl:param name="inheritedControlaccesses"/>
	<xsl:variable name="updatedInheritedOriginations">
		<xsl:choose>
			<xsl:when test='$inheritOrigination = "true" and ./did/origination'>
                            <xsl:call-template name="creator">
                                <xsl:with-param name="originations" select="./did/origination"></xsl:with-param>
                            </xsl:call-template>
                        </xsl:when>
			<xsl:otherwise><xsl:copy-of select="$inheritedOriginations"/></xsl:otherwise>
                </xsl:choose>              
        </xsl:variable>	
	<xsl:variable name="updatedInheritedLanguages">
		<xsl:choose>
			<xsl:when test='$inheritLanguage = "true" and ./did/langmaterial'>
	   			<xsl:call-template name="language">
					<xsl:with-param name="langmaterials" select="./did/langmaterial"></xsl:with-param>
				</xsl:call-template>	
			</xsl:when>
			<xsl:otherwise><xsl:copy-of select="$inheritedLanguages"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="updatedInheritedCustodhists">
		<xsl:choose>
			<xsl:when test='$inheritCustodhist = "true" and ./custodhist'>
	   			<xsl:call-template name="custodhist">
					<xsl:with-param name="custodhists" select="./custodhist"></xsl:with-param>
				</xsl:call-template>	
			</xsl:when>
			<xsl:otherwise><xsl:copy-of select="$inheritedCustodhists"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="updatedInheritedAltformavailHeads">
		<xsl:choose>
			<xsl:when test='$inheritAltformavailHead = "true" and ./altformavail/head'>
	   			<xsl:call-template name="altformavailHead">
                                    <xsl:with-param name="altformavailHeads" select="./altformavail/head"></xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise><xsl:copy-of select="$inheritedAltformavailHeads"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="updatedInheritedControlaccesses">
		<xsl:choose>
			<xsl:when test='$inheritControlaccess = "true" and ./controlaccess'>
	   			<xsl:call-template name="controlaccess">
					<xsl:with-param name="controlaccesses" select="./controlaccess"></xsl:with-param>
				</xsl:call-template>	
			</xsl:when>
			<xsl:otherwise><xsl:copy-of select="$inheritedControlaccesses"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

        <xsl:if test="count(child::c) > 0">
	  	<xsl:apply-templates select="c">
			<xsl:with-param name="inheritedOriginations" select="$updatedInheritedOriginations"/>
		   	<xsl:with-param name="inheritedLanguages" select="$updatedInheritedLanguages"/>
                        <xsl:with-param name="inheritedCustodhists" select="$updatedInheritedCustodhists"/>
                        <xsl:with-param name="inheritedAltformavailHeads" select="$updatedInheritedAltformavailHeads"/>
                        <xsl:with-param name="inheritedControlaccesses" select="$updatedInheritedControlaccesses"/>
	  	</xsl:apply-templates>
	</xsl:if>
	<xsl:call-template name="addrecord">
		<xsl:with-param name="currentnode" select="."/>		
		<xsl:with-param name="inheritedOriginations" select="$updatedInheritedOriginations"/>
		<xsl:with-param name="inheritedLanguages" select="$updatedInheritedLanguages"/>
		<xsl:with-param name="inheritedCustodhists" select="$updatedInheritedCustodhists"/>
                <xsl:with-param name="inheritedAltformavailHeads" select="$updatedInheritedAltformavailHeads"/>
                <xsl:with-param name="inheritedControlaccesses" select="$updatedInheritedControlaccesses"/>
	</xsl:call-template>	
</xsl:template>
<!-- This template creates a record if a element did/dao is found.-->
<xsl:template name="addrecord">
	<xsl:param name="currentnode"/>
	<xsl:param name="inheritedOriginations"/>
	<xsl:param name="inheritedLanguages"/>
        <xsl:param name="inheritedCustodhists"/>	
	<xsl:param name="inheritedAltformavailHeads"/>
        <xsl:param name="inheritedControlaccesses"/>
        <!-- for each dao found, create a record element --> 
	<!-- <xsl:for-each select='did/dao[not(@xlink:role="THUMBNAIL")]'>-->
	<xsl:for-each select='did/dao[not(@xlink:title="thumbnail")]'>
			<xsl:variable name="linkPosition" select="position()"/>
			<xsl:variable name="cnode" select="current()/parent::node()/parent::node()"/>
			<xsl:variable name="didnode" select="current()/parent::node()"/>
			<xsl:variable name="parentcnode" select="$cnode/parent::node()"/>
			<xsl:variable name="inheritFromParent" select='$cnode[@level="item"] and $parentcnode[@level="file"] and $inheritElementsFromFileLevel="true"'/>
			<record>
				<!-- <ead><eadheader><eadid> and <parent did of dao><unitid @type="call number">-->
				<dc:identifier><xsl:apply-templates select="$didnode/unitid"/></dc:identifier>
				<xsl:if test='/ead/archdesc/did/repository'>
					<dc:source><xsl:value-of select="/ead/archdesc/did/repository/text()"/></dc:source>
				</xsl:if>
				<xsl:if test='/ead/eadheader/filedesc/publicationstmt/publisher'>
                                    <dc:publisher><xsl:value-of select="/ead/eadheader/filedesc/publicationstmt/publisher/text()"/></dc:publisher>
                                </xsl:if>
                                <xsl:if test='/ead/eadheader/filedesc/publicationstmt/date'>
                                    <dcterms:issued><xsl:value-of select="/ead/eadheader/filedesc/publicationstmt/date/text()"/></dcterms:issued>
                                </xsl:if>
                                <xsl:choose>
					<xsl:when test="$inheritFromParent">
						<xsl:variable name="parentdidnode" select="$parentcnode/did"/>
						<xsl:variable name="parentofparentcnode" select="$parentcnode/parent::node()"/>
						<dc:title><xsl:value-of select="$parentcnode/did/unittitle[1]"/> >> <xsl:value-of select="$didnode/unittitle"/></dc:title>
						<dcterms:alternative><xsl:value-of select="$contextInformationPrefix"/><xsl:call-template name="generaterelation">
							<xsl:with-param name="node" select="$parentofparentcnode"/>
						</xsl:call-template></dcterms:alternative>
						<xsl:choose>
							<xsl:when test='$didnode/abstract[@encodinganalog="summary"]'>
								<dc:description><xsl:value-of select='$didnode/abstract[@encodinganalog="summary"]'/></dc:description>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test='$parentdidnode/abstract[@encodinganalog="summary"]'>
									<dc:description><xsl:value-of select='$parentdidnode/abstract[@encodinganalog="summary"]'/></dc:description>
								</xsl:if>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:choose>
							<xsl:when test='$cnode/scopecontent[@encodinganalog="summary"]'>
								<dc:description><xsl:value-of select='$cnode/scopecontent[@encodinganalog="summary"]/p'/> <xsl:value-of select='$parentcnode/scopecontent[@encodinganalog="summary"]/head'/></dc:description>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test='$parentcnode/scopecontent[@encodinganalog="summary"]/p'>
									<dc:description><xsl:value-of select='$parentcnode/scopecontent[@encodinganalog="summary"]/p'/></dc:description>
								</xsl:if>
								<xsl:if test='$parentcnode/scopecontent[@encodinganalog="summary"]/head'>
									<dc:description><xsl:value-of select='$parentcnode/scopecontent[@encodinganalog="summary"]/head'/></dc:description>
								</xsl:if>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:choose>
							<xsl:when test='$didnode/physdesc/physfacet'>
								<dc:format><xsl:value-of select="$didnode/physdesc/physfacet"/></dc:format>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test='$parentdidnode/physdesc/physfacet'>
									<dc:format><xsl:value-of select="$parentdidnode/physdesc/physfacet"/></dc:format>
								</xsl:if>
							</xsl:otherwise>
						</xsl:choose>	
						<xsl:choose>
							<xsl:when test='$didnode/materialspec'>
								<dc:format><xsl:value-of select="$didnode/materialspec"/></dc:format>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test='$parentdidnode/materialspec'>
									<dc:format><xsl:value-of select="$parentdidnode/materialspec"/></dc:format>
								</xsl:if>
							</xsl:otherwise>
						</xsl:choose>	
						<xsl:choose>
							<xsl:when test='$didnode/physdesc/extent'>
								<dcterms:extent><xsl:value-of select="$didnode/physdesc/extent"/></dcterms:extent>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test='$parentdidnode/physdesc/extent'>
									<dcterms:extent><xsl:value-of select="$parentdidnode/physdesc/extent"/></dcterms:extent>
								</xsl:if>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:choose>
							<xsl:when test='$didnode/physdesc/dimensions'>
								<dcterms:extent><xsl:value-of select="$didnode/physdesc/dimensions"/></dcterms:extent>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test='$parentdidnode/physdesc/dimensions'>
									<dcterms:extent><xsl:value-of select="$parentdidnode/physdesc/dimensions"/></dcterms:extent>
								</xsl:if>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:choose>
							<xsl:when test='$cnode/bibliography/bibref'>
								<dc:relation><xsl:value-of select="$cnode/bibliography/bibref/text()"/> </dc:relation>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test='$parentcnode/bibliography/bibref'>
									<dc:relation><xsl:value-of select="$parentcnode/bibliography/bibref/text()"/> </dc:relation>
								</xsl:if>
							</xsl:otherwise>
						</xsl:choose>	
						<xsl:choose>
							<xsl:when test='$didnode/origination'>
                                                        <xsl:call-template name="creator">
                                                            <xsl:with-param name="originations" select="$didnode/origination"></xsl:with-param>
                                                        </xsl:call-template>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:choose>
                                                            <xsl:when test='$parentdidnode/origination'>
                                                                <xsl:call-template name="creator">
                                                                    <xsl:with-param name="originations" select="$parentdidnode/origination"></xsl:with-param>
                                                                </xsl:call-template>
                                                            </xsl:when>
                                                            <xsl:otherwise>
                                                                <xsl:if test='$inheritOrigination = "true" and fn:string-length($inheritedOriginations) > 0'>
                                                                    <xsl:copy-of select="$inheritedOriginations"/>
                                                                </xsl:if>
                                                            </xsl:otherwise>
                                                        </xsl:choose>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                                <xsl:choose>
							<xsl:when test='$didnode/unitdate'>
								<xsl:apply-templates select="$didnode/unitdate"/>	
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="$parentdidnode/unitdate"/>	
							</xsl:otherwise>
						</xsl:choose>
						<xsl:choose>
							<xsl:when test='$cnode/custodhist'>
                                                        <xsl:call-template name="custodhist">
                                                            <xsl:with-param name="custodhists" select="$cnode/custodhist"></xsl:with-param>
                                                        </xsl:call-template>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:choose>
                                                            <xsl:when test='$parentcnode/custodhist'>
                                                                <xsl:call-template name="custodhist">
                                                                    <xsl:with-param name="custodhists" select="$parentcnode/custodhist"></xsl:with-param>
                                                                </xsl:call-template>
                                                            </xsl:when>
                                                            <xsl:otherwise>
                                                                <xsl:if test='$inheritCustodhist = "true" and fn:string-length($inheritedCustodhists) > 0'>
                                                                    <xsl:copy-of select="$inheritedCustodhists"/>
                                                                </xsl:if>
                                                            </xsl:otherwise>
                                                        </xsl:choose>
                                                    </xsl:otherwise>
                                                </xsl:choose>
						<xsl:choose>
                                                    <xsl:when test='$cnode/altformavail/head'>
                                                        <xsl:call-template name="altformavailHead">
                                                            <xsl:with-param name="altformavailHeads" select="$cnode/altformavail/head"></xsl:with-param>
                                                        </xsl:call-template>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:choose>
                                                            <xsl:when test='$parentcnode/altformavail/head'>
                                                                <xsl:call-template name="altformavailHead">
                                                                    <xsl:with-param name="altformavailHeads" select="$parentcnode/altformavail/head"></xsl:with-param>
                                                                </xsl:call-template>
                                                            </xsl:when>
                                                            <xsl:otherwise>
                                                                <xsl:if test='$inheritAltformavailHead = "true" and fn:string-length($inheritedAltformavailHeads) > 0'>
                                                                    <xsl:copy-of select="$inheritedAltformavailHeads"/>
                                                                </xsl:if>
                                                            </xsl:otherwise>
                                                        </xsl:choose>
                                                    </xsl:otherwise>
                                                </xsl:choose>
						<xsl:choose>
							<xsl:when test='$cnode/controlaccess'>
                                                        <xsl:call-template name="controlaccess">
                                                            <xsl:with-param name="controlaccesses" select="$cnode/controlaccess"></xsl:with-param>
                                                        </xsl:call-template>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:choose>
                                                            <xsl:when test='$parentcnode/controlaccess'>
                                                                <xsl:call-template name="controlaccess">
                                                                    <xsl:with-param name="controlaccesses" select="$parentcnode/controlaccess"></xsl:with-param>
                                                                </xsl:call-template>
                                                            </xsl:when>
                                                            <xsl:otherwise>
                                                                <xsl:if test='$inheritControlaccess = "true" and fn:string-length($inheritedControlaccesses) > 0'>
                                                                    <xsl:copy-of select="$inheritedControlaccesses"/>
                                                                </xsl:if>
                                                            </xsl:otherwise>
                                                        </xsl:choose>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                        </xsl:when>
                                        
					<xsl:otherwise>
						<dc:title><xsl:value-of select="$didnode/unittitle"/></dc:title>
						<dcterms:alternative><xsl:value-of select="$contextInformationPrefix"/><xsl:call-template name="generaterelation">
							<xsl:with-param name="node" select="$parentcnode"/>
						</xsl:call-template></dcterms:alternative>
						<xsl:if test='$didnode/abstract[@encodinganalog="summary"]'>
							<dc:description><xsl:value-of select='$didnode/abstract[@encodinganalog="summary"]'/></dc:description>
						</xsl:if>
						<xsl:if test='$cnode/scopecontent[@encodinganalog="summary"]/p'>
								<dc:description><xsl:value-of select='$cnode/scopecontent[@encodinganalog="summary"]/p'/></dc:description>
						</xsl:if>
						<xsl:if test='$cnode/scopecontent[@encodinganalog="summary"]/head'>
								<dc:description><xsl:value-of select='$cnode/scopecontent[@encodinganalog="summary"]/head'/></dc:description>
						</xsl:if>
						<xsl:if test="$didnode/physdesc/physfacet"><dc:format><xsl:value-of select="$didnode/physdesc/physfacet"/></dc:format></xsl:if>
						<xsl:if test="$didnode/materialspec"><dc:format><xsl:value-of select="$didnode/materialspec"/></dc:format></xsl:if>
						<xsl:if test="$didnode/physdesc/extent"><dcterms:extent><xsl:value-of select="$didnode/physdesc/extent"/> </dcterms:extent></xsl:if>
						<xsl:if test="$didnode/physdesc/dimensions"><dcterms:extent><xsl:value-of select="$didnode/physdesc/dimensions"/> </dcterms:extent></xsl:if>
						<xsl:if test="$cnode/bibliography/bibref"><dc:relation><xsl:value-of select="$cnode/bibliography/bibref/text()"/> </dc:relation></xsl:if>
						<xsl:choose>
							<xsl:when test="$didnode/origination">
                                                        <xsl:call-template name="creator">
                                                            <xsl:with-param name="originations" select="$didnode/origination"></xsl:with-param>
                                                        </xsl:call-template>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:if test='$inheritOrigination = "true" and fn:string-length($inheritedOriginations) > 0'>
                                                            <xsl:copy-of select="$inheritedOriginations"/>
                                                        </xsl:if>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                                <xsl:apply-templates select="$didnode/unitdate"/>
                                                <xsl:choose>
							<xsl:when test="$cnode/custodhist">
                                                        <xsl:call-template name="custodhist">
                                                            <xsl:with-param name="custodhists" select="$cnode/custodhist"></xsl:with-param>
                                                        </xsl:call-template>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:if test='$inheritCustodhist = "true" and fn:string-length($inheritedCustodhists) > 0'>
                                                            <xsl:copy-of select="$inheritedCustodhists"/>
                                                        </xsl:if>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                                <xsl:choose>
                                                    <xsl:when test="$cnode/altformavail/head">
                                                        <xsl:call-template name="altformavailHead">
                                                            <xsl:with-param name="altformavailHeads" select="$cnode/altformavail/head"></xsl:with-param>
                                                        </xsl:call-template>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:if test='$inheritAltformavailHead = "true" and fn:string-length($inheritedAltformavailHeads) > 0'>
                                                            <xsl:copy-of select="$inheritedAltformavailHeads"/>
                                                        </xsl:if>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                                <xsl:choose>
                                                    <xsl:when test="$cnode/controlaccess">
                                                        <xsl:call-template name="controlaccess">
                                                            <xsl:with-param name="controlaccesses" select="$cnode/controlaccess"></xsl:with-param>
                                                        </xsl:call-template>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:if test='$inheritControlaccess = "true" and fn:string-length($inheritedControlaccesses) > 0'>
                                                            <xsl:copy-of select="$inheritedControlaccesses"/>
                                                        </xsl:if>
                                                    </xsl:otherwise>
                                                </xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
						
				
								
				<xsl:choose>
					<xsl:when test="fn:string-length($language) > 0">
						<dc:language><xsl:value-of select="$language"/></dc:language>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="$didnode/langmaterial">
			   					<xsl:call-template name="language">
									<xsl:with-param name="langmaterials" select="$didnode/langmaterial"></xsl:with-param>
								</xsl:call-template>								
							</xsl:when>
							<xsl:when test='$inheritLanguage = "true"'>
								<xsl:choose>
									<xsl:when test='fn:string-length($inheritedLanguages) > 0'>
										<xsl:copy-of select="$inheritedLanguages"/>
									</xsl:when>
									<xsl:otherwise>
										<dc:language><xsl:text>unknown</xsl:text></dc:language>
									</xsl:otherwise>
								</xsl:choose>

							</xsl:when>
							<xsl:when test='$inheritFromParent'>
								<xsl:choose>
									<xsl:when test='$parentcnode/did/langmaterial'>
				   						<xsl:call-template name="language">
											<xsl:with-param name="langmaterials" select="$parentcnode/did/langmaterial"></xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<dc:language><xsl:text>unknown</xsl:text></dc:language>
									</xsl:otherwise>
								</xsl:choose>							
							</xsl:when>		
							<xsl:otherwise>
								<dc:language><xsl:text>unknown</xsl:text></dc:language>
							</xsl:otherwise>																			
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="./@xlink:role">
						<dc:type><xsl:call-template name="convertToDcType">
						<xsl:with-param name="role" select="./@xlink:role"></xsl:with-param>
						</xsl:call-template></dc:type>
					</xsl:when>
					<xsl:otherwise>
						<dc:type><xsl:call-template name="convertToDcType">
						<xsl:with-param name="role" select="$europeana_type"></xsl:with-param>
						</xsl:call-template></dc:type>
					</xsl:otherwise>
					</xsl:choose>	
				<xsl:choose>
					<xsl:when test="$inheritFromParent">
						<xsl:variable name="parentofparentcnode" select="$parentcnode/parent::node()"/>
							<xsl:choose>
								<xsl:when test='$cnode/controlaccess/geogname'>
									<xsl:apply-templates select="$cnode/controlaccess/geogname"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test='$parentofparentcnode/controlaccess/geogname'>
										<xsl:apply-templates select="$parentofparentcnode/controlaccess/geogname"/>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>		
							<xsl:choose>
								<xsl:when test='$cnode/index/indexentry/geogname'>
									<xsl:apply-templates select="$cnode/index/indexentry/geogname"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test='$parentofparentcnode/index/indexentry/geogname'>
										<xsl:apply-templates select="$parentofparentcnode/index/indexentry/geogname"/>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:if test="$dc_rights">
							<dc:rights><xsl:value-of select="$dc_rights"/></dc:rights>
							</xsl:if>	
							<xsl:choose>
								<xsl:when test='$cnode/bioghist/p'>
									<europeana:unstored><xsl:value-of select="$cnode/bioghist/p"/></europeana:unstored>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test='$parentofparentcnode/bioghist/p'>
										<europeana:unstored><xsl:value-of select="$parentofparentcnode/bioghist/p"/></europeana:unstored>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>	
							<xsl:choose>
								<xsl:when test='$cnode/bioghist/head'>
									<europeana:unstored><xsl:value-of select="$cnode/bioghist/head"/></europeana:unstored>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test='$parentofparentcnode/bioghist/head'>
										<europeana:unstored><xsl:value-of select="$parentofparentcnode/bioghist/head"/></europeana:unstored>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>	
							<xsl:choose>
								<xsl:when test='$cnode/phystech/p'>
									<europeana:unstored><xsl:value-of select="$cnode/phystech/p"/></europeana:unstored>
								</xsl:when>
								<xsl:otherwise>
									<xsl:if test='$parentofparentcnode/phystech/p'>
										<europeana:unstored><xsl:value-of select="$parentofparentcnode/phystech/p"/></europeana:unstored>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>																																				
					</xsl:when>
					<xsl:otherwise>
					    <xsl:apply-templates select="$cnode/controlaccess/geogname"/>
					    <xsl:apply-templates select="$cnode/index/indexentry/geogname"/>
						<xsl:if test="$dc_rights">
							<dc:rights><xsl:value-of select="$dc_rights"/></dc:rights>
						</xsl:if>						
					    <xsl:if test='$cnode/bioghist/p'><europeana:unstored><xsl:value-of select="$cnode/bioghist/p"/></europeana:unstored></xsl:if>
					    <xsl:if test='$cnode/bioghist/head'><europeana:unstored><xsl:value-of select="$cnode/bioghist/head"/></europeana:unstored></xsl:if>
					    <xsl:if test='$cnode/phystech/p'><europeana:unstored><xsl:value-of select="$cnode/phystech/p"/></europeana:unstored></xsl:if>					
					</xsl:otherwise>
				</xsl:choose>			

			    <xsl:if test='/ead/archdesc/did/abstract[@encodinganalog="summary"]'><europeana:unstored><xsl:value-of select='/ead/archdesc/did/abstract[@encodinganalog="summary"]'/></europeana:unstored></xsl:if>

				<!-- <xsl:variable name="thumbnail" select='$didnode/dao[@xlink:role="THUMBNAIL"]'/>-->
				<xsl:variable name="thumbnail" select='$didnode/dao[@xlink:title="thumbnail"]'/> 
				<!-- if thumbnail exists --> 
				<xsl:if test="$thumbnail">
					<!-- if more than one thumbnail exists --> 
					<europeana:object><xsl:choose>
						<xsl:when test="count($thumbnail) >= $linkPosition"><xsl:value-of select="$thumbnail[$linkPosition]/@xlink:href"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$thumbnail[1]/@xlink:href" /> </xsl:otherwise>
					</xsl:choose></europeana:object>
				</xsl:if>
				<europeana:provider><xsl:value-of select="$europeana_provider"/></europeana:provider>
					<xsl:choose>
						<xsl:when test="./@xlink:role">
						<xsl:choose>
							<xsl:when test=' "TEXT" eq fn:string(@xlink:role)'>
								<europeana:type><xsl:text>TEXT</xsl:text></europeana:type>
							</xsl:when>
							<xsl:when test=' "IMAGE" eq fn:string(@xlink:role)'>
								<europeana:type><xsl:text>IMAGE</xsl:text></europeana:type>
							</xsl:when>
							<xsl:when test=' "SOUND" eq fn:string(@xlink:role)'>
								<europeana:type><xsl:text>SOUND</xsl:text></europeana:type>
							</xsl:when>		
							<xsl:when test=' "VIDEO" eq fn:string(@xlink:role)'>
								<europeana:type><xsl:text>VIDEO</xsl:text></europeana:type>
							</xsl:when>									
							<xsl:otherwise>
								<europeana:type><xsl:value-of select="$europeana_type"/></europeana:type>
							</xsl:otherwise>
						</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<europeana:type><xsl:value-of select="$europeana_type"/></europeana:type>
						</xsl:otherwise>
					</xsl:choose>				
				<europeana:rights><xsl:value-of select="$europeana_rights"/></europeana:rights>		
				<xsl:choose>
					<xsl:when test="$europeana_dataprovider">
						<europeana:dataProvider><xsl:value-of select="$europeana_dataprovider"/></europeana:dataProvider>
					</xsl:when>
					<xsl:otherwise><europeana:dataProvider><xsl:value-of select="/ead/archdesc/did/repository/text()"/></europeana:dataProvider></xsl:otherwise>
				</xsl:choose>				
				<europeana:isShownAt><xsl:value-of select="@xlink:href"/></europeana:isShownAt>
			</record>
	</xsl:for-each>
</xsl:template>
<xsl:template name="generaterelation">
	<xsl:param name="node"/>
        <xsl:variable name="eadid" select="/ead/eadheader/eadid/@identifier"/>
			<xsl:choose>
				<xsl:when test=' "dsc" eq fn:string(fn:node-name($node))'>
					<xsl:variable name="archdesc" select="$node/parent::node()"/>
					<xsl:value-of select="$eadid"/><xsl:text> </xsl:text><xsl:value-of select="normalize-space(/ead/eadheader/filedesc/titlestmt/titleproper[1])"/><xsl:text> >> </xsl:text><xsl:value-of select="$eadid"/><xsl:text>_</xsl:text><xsl:value-of select="fn:replace(normalize-space($archdesc/did/unitid), '[\n\t\r ]', '')"/><xsl:text> </xsl:text><xsl:value-of select="$archdesc/did/unittitle"/>
				</xsl:when>	
				<xsl:otherwise>
					<xsl:call-template name="generaterelation">
					    <xsl:with-param name="node" select="$node/parent::node()"/>
					</xsl:call-template><xsl:text> >> </xsl:text><xsl:value-of select="$eadid"/><xsl:text>_</xsl:text><xsl:value-of select="$node/@id"/><xsl:text> </xsl:text><xsl:value-of select="normalize-space($node/did/unittitle[1])"/>
				</xsl:otherwise>
			</xsl:choose>				

</xsl:template>
<xsl:template match='unitid'>
	<xsl:if test='@type="call number"'>
		<xsl:value-of select="."/>
	</xsl:if>		
</xsl:template>
<xsl:template name='language'>
	<xsl:param name="langmaterials"/>
	<xsl:for-each select="$langmaterials">
		<xsl:variable name="languageWithoutSpaces" ><xsl:value-of select="fn:replace(normalize-space(.), '[\n\t\r]', '')"/></xsl:variable>
		<dc:language><xsl:choose>
			<xsl:when test="fn:string-length($languageWithoutSpaces) > 0"><xsl:value-of select="$languageWithoutSpaces"/></xsl:when>
			<xsl:otherwise><xsl:text>unknown</xsl:text></xsl:otherwise>
		</xsl:choose>
		</dc:language>
	</xsl:for-each>
</xsl:template>
<xsl:template name='creator'>
	<xsl:param name="originations"/>
	<xsl:for-each select="$originations">
            <xsl:choose>
                <xsl:when test='./@label="pre"'>
                    <dcterms:provenance>
                        <xsl:call-template name="generateOriginationContent">
                            <xsl:with-param name="node" select="."/>
                        </xsl:call-template>
                    </dcterms:provenance>
                </xsl:when>
                <xsl:otherwise>
                    <dc:creator>
                        <xsl:call-template name="generateOriginationContent">
                            <xsl:with-param name="node" select="."/>
                        </xsl:call-template>
                    </dc:creator>
                </xsl:otherwise>
            </xsl:choose>
	</xsl:for-each>	
</xsl:template>
<xsl:template name='generateOriginationContent'>
	<xsl:param name="node"/>
		<xsl:for-each select="$node/persname">
			<xsl:value-of select="$node"/>
		</xsl:for-each>
		<xsl:for-each select="$node/corpname">
			<xsl:value-of select="$node"/>
		</xsl:for-each>	
		<xsl:for-each select="$node/famname">
			<xsl:value-of select="$node"/>
		</xsl:for-each>	
		<xsl:for-each select="$node/name">
			<xsl:value-of select="$node"/>
		</xsl:for-each>	
		<xsl:for-each select="$node/text()">
			<xsl:variable name="text"><xsl:value-of select="fn:replace(normalize-space($node), '[\n\t\r]', '')"/></xsl:variable>
			<xsl:if test="fn:string-length($text) > 0">
				<xsl:value-of select="$text"/>
			</xsl:if>
		</xsl:for-each>
</xsl:template>
<xsl:template name='custodhist'>
    <xsl:param name="custodhists"/>
    <xsl:for-each select="$custodhists">
        <dcterms:provenance>
            <xsl:choose>
                <xsl:when test="./head">
                    <xsl:value-of select="./head"/>
                </xsl:when>
                <xsl:when test="./p">
                    <xsl:value-of select="./p"/>
                </xsl:when>
                <xsl:otherwise><xsl:text>Other custodhist elements should not be mapped</xsl:text></xsl:otherwise>
            </xsl:choose>
        </dcterms:provenance>
    </xsl:for-each>
</xsl:template>
<xsl:template match='geogname'>
	<dcterms:spatial><xsl:value-of select="."/></dcterms:spatial>
</xsl:template>
<xsl:template match='unitdate'>
	<xsl:choose>
		<xsl:when test='$useISODates="true"'>
			<xsl:choose>
				<xsl:when test='./@era="ce" and ./@normal'> 
					<xsl:analyze-string select="./@normal" 
					    regex="(\d\d\d\d(-\d\d(-\d\d)?)?)(/(\d\d\d\d(-\d\d(-\d\d)?)?))?">
					    <xsl:matching-substring>
						        <xsl:variable name="startdate"><xsl:value-of select="regex-group(1)"/></xsl:variable>
						        <xsl:variable name="enddate"><xsl:value-of select="regex-group(5)"/></xsl:variable>
						        <dc:date>
						        <xsl:if test="fn:string-length($startdate) > 0">
						        	<xsl:value-of select="$startdate"/>
						        </xsl:if>
						        <xsl:if test="fn:string-length($startdate) > 0 and fn:string-length($enddate) > 0">
						        	<xsl:text> - </xsl:text>
						        </xsl:if>
						        <xsl:if test="fn:string-length($enddate) > 0">
						        	<xsl:value-of select="$enddate"/>
						        </xsl:if>	
						        </dc:date>		        
					    </xsl:matching-substring>    
					</xsl:analyze-string>		
				</xsl:when>
				<xsl:otherwise>
					<xsl:if test='fn:string-length(normalize-space(.)) > 0'>
						<xsl:variable name="notNormalizedDate">NOT_NORMALIZED_DATE:<xsl:value-of select="normalize-space(.)"/></xsl:variable>
						<xsl:message><xsl:value-of select="$notNormalizedDate"/></xsl:message>
						<dc:date><xsl:value-of select="$notNormalizedDate"/></dc:date>
					</xsl:if>	
				</xsl:otherwise>
			</xsl:choose> 		
		</xsl:when>
		<xsl:otherwise>
			<xsl:if test='fn:string-length(normalize-space(.)) > 0'>
				<dc:date><xsl:value-of select="normalize-space(.)"/></dc:date>
			</xsl:if>	
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>
<xsl:template name="convertToDcType">
	<xsl:param name="role"/>
	<xsl:choose>
		<xsl:when test=' "TEXT" eq fn:string($role)'>
			<xsl:text>Text</xsl:text>
		</xsl:when>
		<xsl:when test=' "IMAGE" eq fn:string($role)'>
		<xsl:text>Image</xsl:text>
		</xsl:when>
		<xsl:when test=' "SOUND" eq fn:string($role)'>
			<xsl:text>Sound</xsl:text>
		</xsl:when>		
		<xsl:when test=' "VIDEO" eq fn:string($role)'>
			<xsl:text>MovingImage</xsl:text>
		</xsl:when>	
		<xsl:otherwise><xsl:call-template name="convertToDcType">
						<xsl:with-param name="role" select="$europeana_type"></xsl:with-param>
						</xsl:call-template></xsl:otherwise>
	</xsl:choose>	
</xsl:template>
<xsl:template name="altformavailHead">
    <xsl:param name="altformavailHeads"/>
    <xsl:for-each select="$altformavailHeads">
        <dcterms:hasFormat>
            <xsl:value-of select="."/>
        </dcterms:hasFormat>
    </xsl:for-each>
</xsl:template>
<xsl:template name="controlaccess">
    <xsl:param name="controlaccesses"/>
    <xsl:for-each select="$controlaccesses">
		<xsl:for-each select="$controlaccesses/persname">
                    <dc:coverage>
                        <xsl:value-of select="."/>
                    </dc:coverage>
		</xsl:for-each>
		<xsl:for-each select="$controlaccesses/corpname">
                    <dc:coverage>
                        <xsl:value-of select="."/>
                    </dc:coverage>
		</xsl:for-each>	
		<xsl:for-each select="$controlaccesses/famname">
                    <dc:coverage>
                        <xsl:value-of select="."/>
                    </dc:coverage>
		</xsl:for-each>	
		<xsl:for-each select="$controlaccesses/name">
                    <dc:coverage>
                        <xsl:value-of select="."/>
                    </dc:coverage>
		</xsl:for-each>
		<xsl:for-each select="$controlaccesses/subject">
                    <dc:subject>
                        <xsl:value-of select="."/>
                    </dc:subject>
		</xsl:for-each>
    </xsl:for-each>
</xsl:template>
</xsl:stylesheet>
