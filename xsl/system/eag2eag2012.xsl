<?xml version="1.0" encoding="UTF-8"?>
<!-- EAG 2012 creation from original EAG -->
<xsl:stylesheet version="2.0"
	xmlns="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ape="http://www.archivesportaleurope.net/functions"
	xmlns:ape_static="http://www.archivesportaleurope.net/functions_static"
	xsi:schemaLocation="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/ http://www.archivesportaleurope.net/Portal/profiles/eag_2012.xsd"
	xpath-default-namespace="http://www.archivesportaleurope.net/Portal/profiles/eag_2012/"
	exclude-result-prefixes="xsl xs ape ape_static">

	<xsl:output encoding="utf-8" indent="yes" method="xml" />

	<!-- Options to be asked by the converter -->
	<xsl:param name="xmlLang" select="'eng'" />
	<xsl:param name="repositoryCode" />
	<!--xsl:param name="repositoryRole" select="'Head quarter'" /-->  <!-- @WP4: Same question as above - $WP4: selection from the 3 possibilities
		(ie drop-down-list) - Yoann: It is a stylesheet, not a form. There won't 
		be any drop-down list. -->

	<xsl:variable name="unitList"
		select="('squaremetre', 'linearmetre', 'site', 'book', 'title')" />

	<xsl:template match="/">
		<eag>
			<xsl:attribute name="xsi:schemaLocation"
				select="'http://www.archivesportaleurope.net/Portal/profiles/eag_2012/ http://www.archivesportaleurope.net/Portal/profiles/eag_2012.xsd'" />
			<xsl:attribute name="audience"
				select="if(*:eag/@audience) then *:eag/@audience else 'external'" />
			<xsl:apply-templates select="*:eag/*:eagheader" />
			<xsl:apply-templates select="*:eag/*:archguide" />
			<xsl:call-template name="relations">
				<xsl:with-param name="repositorguides"
					select="*:eag/*:archguide/*:desc/*:repositorguides" />
			</xsl:call-template>
		</eag>
	</xsl:template>

	<xsl:template match="*" name="excludeElement" mode="#all">
		<xsl:message>
			Element has been excluded:
			<xsl:value-of select="current()/name()" />
		</xsl:message>
	</xsl:template>

	<xsl:template match="*:eagheader">
		<control>
			<xsl:choose>
				<xsl:when test="$repositoryCode">
					<recordId>
						<xsl:value-of select="$repositoryCode" />
					</recordId>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="*:eagid" />
				</xsl:otherwise>
			</xsl:choose>
			<xsl:call-template name="maintenanceAgencyTemplate">
				<xsl:with-param name="repositorycode"
					select="following-sibling::*[name()='archguide']/*:identity/*:repositorid/@repositorycode" />
				<xsl:with-param name="agencyname"
					select="following-sibling::*[name()='archguide']/*:identity/*:autform/text()" />
			</xsl:call-template>
			<xsl:call-template name="maintenanceStatusTemplate">
				<xsl:with-param name="status" select="@status" />
			</xsl:call-template>
			<xsl:apply-templates select="*:mainhist" />
			<xsl:if test="count(*:languagedecl) gt 0">
				<languageDeclarations>
					<xsl:apply-templates select="*:languagedecl" />
				</languageDeclarations>
			</xsl:if>
			<xsl:for-each
				select="('EAG', 'ISDIAH', @langencoding, @countryencoding, @dateencoding, @repositoryencoding, @scriptencoding)">
				<xsl:call-template name="conventionDeclarationTemplate">
					<xsl:with-param name="encoding" select="current()" />
				</xsl:call-template>
			</xsl:for-each>
			<xsl:call-template name="source4mainevent">
				<xsl:with-param name="mainhist" select="*:mainhist" />
			</xsl:call-template>
		</control>
	</xsl:template>

	<xsl:template name="maintenanceAgencyTemplate">
		<xsl:param name="repositorycode" />
		<xsl:param name="agencyname" />
		<maintenanceAgency>
			<agencyCode>
				<xsl:value-of select="$repositorycode" />
			</agencyCode>
			<agencyName>
				<xsl:value-of select="$agencyname" />
			</agencyName>
		</maintenanceAgency>
	</xsl:template>

	<xsl:template name="maintenanceStatusTemplate">
		<xsl:param name="status" />
		<maintenanceStatus>
			<xsl:value-of
				select="if($status eq 'edited') then 'revised' else if($status eq 'draft') then 'new' else $status" />
		</maintenanceStatus>
	</xsl:template>

	<xsl:template name="conventionDeclarationTemplate">
		<xsl:param name="encoding" />

		<xsl:if test="exists($encoding)">
			<conventionDeclaration>
				<xsl:choose>
					<xsl:when test="$encoding eq 'EAG'">
						<abbreviation>
							<xsl:value-of select="'EAG'" />
						</abbreviation>
						<citation>
							<xsl:if test="exists($xmlLang)">
								<xsl:attribute name="xml:lang" select="$xmlLang" />
							</xsl:if>
							<xsl:value-of select="'EAG (Encoded Archival Guide) 2012'" />
						</citation>
					</xsl:when>
					<xsl:when test="$encoding eq 'ISDIAH'">
						<abbreviation>
							<xsl:value-of select="'ISDIAH'" />
						</abbreviation>
						<citation>
							<xsl:if test="exists($xmlLang)">
								<xsl:attribute name="xml:lang" select="$xmlLang" />
							</xsl:if>
							<xsl:value-of
								select="'International Standard for Describing Institutions with Archival Holdings'" />
						</citation>
					</xsl:when>
					<xsl:when test="$encoding eq 'iso639-2b'">
						<abbreviation>
							<xsl:value-of select="'ISO 639-2b'" />
						</abbreviation>
						<citation>
							<xsl:if test="exists($xmlLang)">
								<xsl:attribute name="xml:lang" select="$xmlLang" />
							</xsl:if>
							<xsl:value-of
								select="'Codes for the representation of names of languages — Part 2: Alpha-3 code'" />
						</citation>
					</xsl:when>
					<xsl:when test="$encoding eq 'iso3166-1'">
						<abbreviation>
							<xsl:value-of select="'ISO 3166-1'" />
						</abbreviation>
						<citation>
							<xsl:if test="exists($xmlLang)">
								<xsl:attribute name="xml:lang" select="$xmlLang" />
							</xsl:if>
							<xsl:value-of
								select="'Codes for the representation of names of countries and their subdivisions – Part 1: Country codes'" />
						</citation>
					</xsl:when>
					<xsl:when test="$encoding eq 'iso15924'">
						<abbreviation>
							<xsl:value-of select="'ISO 15924'" />
						</abbreviation>
						<citation>
							<xsl:if test="exists($xmlLang)">
								<xsl:attribute name="xml:lang" select="$xmlLang" />
							</xsl:if>
							<xsl:value-of
								select="'Codes for the representation of names of scripts'" />
						</citation>
					</xsl:when>
					<xsl:when test="$encoding eq 'iso8601'">
						<abbreviation>
							<xsl:value-of select="'ISO 8601'" />
						</abbreviation>
						<citation>
							<xsl:if test="exists($xmlLang)">
								<xsl:attribute name="xml:lang" select="$xmlLang" />
							</xsl:if>
							<xsl:value-of
								select="'Data elements and interchange formats – Information interchange – Representation of dates and times'" />
						</citation>
					</xsl:when>
					<xsl:when test="$encoding eq 'iso15511'">
						<abbreviation>
							<xsl:value-of select="'ISO 15511'" />
						</abbreviation>
						<citation>
							<xsl:if test="exists($xmlLang)">
								<xsl:attribute name="xml:lang" select="$xmlLang" />
							</xsl:if>
							<xsl:value-of
								select="'International Standard Identifier for Libraries and Related Organisations'" />
						</citation>
					</xsl:when>
					<xsl:otherwise>
						<abbreviation>
							<xsl:value-of select="$encoding" />
						</abbreviation>
						<citation>
							<xsl:if test="exists($xmlLang)">
								<xsl:attribute name="xml:lang" select="$xmlLang" />
							</xsl:if>
							<xsl:value-of select="'Unknown archival standard'" />
						</citation>
					</xsl:otherwise>
				</xsl:choose>
			</conventionDeclaration>
		</xsl:if>
	</xsl:template>

	<xsl:template name="source4mainevent">
		<xsl:param name="mainhist" />
		<xsl:if test="exists($mainhist/*:mainevent/*:source/text())">
			<sources>
				<xsl:for-each select="$mainhist/*:mainevent">
					<xsl:if test="exists(*:source/text())">
						<source>
							<sourceEntry>
								<xsl:if test="exists($xmlLang)">
									<xsl:attribute name="xml:lang" select="$xmlLang" />
								</xsl:if>
								<xsl:value-of select="*:source/text()" />
							</sourceEntry>
						</source>
					</xsl:if>
				</xsl:for-each>
			</sources>
		</xsl:if>
	</xsl:template>

	<xsl:template match="*:eagid">
		<recordId>
			<xsl:value-of select="text()" />
		</recordId>
	</xsl:template>

	<xsl:template match="*:mainhist">
		<maintenanceHistory>
			<xsl:for-each select="*:mainevent">
				<xsl:call-template name="oneMainEvent">
					<xsl:with-param name="maintype" select="@maintype" />
					<xsl:with-param name="date" select="*:date" />
					<xsl:with-param name="respevent" select="*:respevent" />
				</xsl:call-template>
			</xsl:for-each>
		</maintenanceHistory>
	</xsl:template>

	<xsl:template name="oneMainEvent">
		<xsl:param name="maintype" />
		<xsl:param name="date" />
		<xsl:param name="respevent" />
		<maintenanceEvent>
			<xsl:if test="exists($xmlLang)">
				<xsl:attribute name="xml:lang" select="$xmlLang" />
			</xsl:if>
			<agent>
				<xsl:if test="exists($xmlLang)">
					<xsl:attribute name="xml:lang" select="$xmlLang" />
				</xsl:if>
				<xsl:value-of
					select="if(exists($respevent/*:charge/text())) then concat($respevent/*:firstname/text(), ' ', $respevent/*:surnames/text(), ', ', $respevent/*:charge/text()) else if(exists($respevent)) then concat($respevent/*:firstname/text(), $respevent/*:surnames/text()) else 'automatically created agent'" />
			</agent>
			<agentType>
				<xsl:value-of select="if(exists($respevent)) then 'human' else 'machine'" />
			</agentType>
			<eventDateTime>
				<xsl:choose>
					<xsl:when
						test="matches(ape:normalizeDate(normalize-space($date/@normal)),'\d{4}-\d{2}-\d{2}')">
						<xsl:attribute name="standardDateTime"
							select="concat(ape:normalizeDate(normalize-space($date/@normal)), 'T23:59:59')" />
						<xsl:value-of select="$date/@normal" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:attribute name="standardDateTime"
							select="concat(ape:normalizeDate(normalize-space($date/@normal)), '-12-31T23:59:59')" />
						<xsl:value-of select="$date/@normal" />
					</xsl:otherwise>
				</xsl:choose>

			</eventDateTime>
			<eventType>
				<xsl:choose>
					<xsl:when test="$maintype eq 'create'">
						<xsl:value-of select="'created'" />
					</xsl:when>
					<xsl:when test="$maintype eq 'update'">
						<xsl:value-of select="'updated'" />
					</xsl:when>
					<xsl:when test="$maintype eq 'delete'">
						<xsl:value-of select="'deleted'" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="'created'" />
					</xsl:otherwise>
				</xsl:choose>
			</eventType>
		</maintenanceEvent>
	</xsl:template>

	<xsl:template match="*:languagedecl">

		<xsl:for-each select="*:language">
			<languageDeclaration>
				<language>
					<xsl:attribute name="languageCode" select="@langcode" />
				</language>
				<script>
					<xsl:attribute name="scriptCode"
						select="if(@scriptcode) then @scriptcode else 'Latn'" />
				</script>
			</languageDeclaration>
		</xsl:for-each>

	</xsl:template>


	<xsl:template match="*:archguide">
		<archguide>
			<xsl:apply-templates select="*:identity, *:desc" />
		</archguide>
	</xsl:template>

	<xsl:template match="*:identity">
		<identity>
			<xsl:apply-templates
				select="*:repositorid, *:autform, *:parform, *:nonpreform" />
		</identity>
	</xsl:template>
	<xsl:template match="*:repositorid">
		<!--<xsl:variable name="isRepositorycodeCorrect" select="ape:testRepositorycode(@repositorycode)"/> --> <!-- Yoann: Create function in java -->
		<xsl:variable name="isRepositorycodeCorrect" select="true()"
			as="xs:boolean" />
		<repositorid>
			<xsl:attribute name="countrycode" select="@countrycode" />
			<xsl:if test="$isRepositorycodeCorrect">
				<xsl:attribute name="repositorycode" select="@repositorycode" /> <!-- Yoann: else ? -->
			</xsl:if>
		</repositorid>
		<!--<xsl:if test="not($isRepositorycodeCorrect)"> --> <!-- no if because otherRepositorId is mandatory --> <!-- @WP4: Should otherRepositorId really be mandatory in the schema? $WP4: 
			optional in eag_2012.xsd, but mandatory for ape - Yoann: How do you want 
			this? You want 2 schemas? EAG_2012 and APE_EAG_2012 ? -->
        <xsl:if test="@repositorycode">
            <otherRepositorId>
                <xsl:value-of select="@repositorycode" />
            </otherRepositorId>
        </xsl:if>
		<!--</xsl:if> -->
	</xsl:template>

	<xsl:template match="*:autform">
		<autform>
			<xsl:if test="exists($xmlLang)">
				<xsl:attribute name="xml:lang" select="$xmlLang" />
			</xsl:if>
			<xsl:value-of select="text()" />
		</autform>
	</xsl:template>

	<xsl:template match="*:parform">
		<xsl:if test="exists(text())">
			<parform>
				<xsl:if test="exists($xmlLang)">
					<xsl:attribute name="xml:lang" select="$xmlLang" />
				</xsl:if>
				<xsl:value-of select="text()" />
			</parform>
		</xsl:if>
	</xsl:template>

	<xsl:template match="*:nonpreform">
		<xsl:if test="exists(text())">
			<nonpreform>
				<xsl:if test="exists($xmlLang)">
					<xsl:attribute name="xml:lang" select="$xmlLang" />
				</xsl:if>
				<xsl:value-of select="text()" />
			</nonpreform>
		</xsl:if>
	</xsl:template>

	<xsl:template match="*:desc">
		<desc>
			<repositories>
				<repository>
                    <xsl:if test="preceding-sibling::*[name()='identity']/*:autform/text()">
                        <repositoryName>
                            <xsl:if test="exists($xmlLang)">
                                <xsl:attribute name="xml:lang" select="$xmlLang" />
                            </xsl:if>
                            <xsl:value-of
                                select="preceding-sibling::*[name()='identity']/*:autform/text()" />
                        </repositoryName>
                    </xsl:if>
					<!--<repositoryRole>-->
						<!--<xsl:value-of select="$repositoryRole" />-->
					<!--</repositoryRole>-->
					<xsl:apply-templates select="*:geogarea" />
					<location localType="visitors address" >
						<xsl:apply-templates select="*:country, *:firstdem, *:secondem" />
						<xsl:call-template name="address">
							<xsl:with-param name="municipality" select="*:municipality" />
							<xsl:with-param name="street" select="*:street" />
							<xsl:with-param name="postalcode" select="*:postalcode" />
							<xsl:with-param name="localentity" select="*:localentity" />
						</xsl:call-template>
					</location>
					<xsl:apply-templates select="*:telephone, *:fax, *:email, *:webpage" />
					<xsl:apply-templates
						select="*:repositorhist, *:repositorfound, *:repositorsup" />
					<xsl:apply-templates select="*:buildinginfo, *:adminhierarchy" />
					<xsl:apply-templates select="*:extent" />
					<xsl:apply-templates select="*:timetable" />
					<xsl:if test="not(*:timetable)">
						<timetable>
							<opening />
						</timetable>
					</xsl:if>
					<xsl:apply-templates select="*:access" />
					<xsl:if test="not(*:access)">
						<access question="no" />
					</xsl:if>
					<xsl:apply-templates select="*:buildinginfo/*:handicapped" />
					<xsl:if test="not(*:buildinginfo/*:handicapped)">
						<accessibility question="no" />
					</xsl:if>
					<xsl:call-template name="services">
						<xsl:with-param name="searchroom" select="*:buildinginfo/*:searchroom" />
						<xsl:with-param name="techservices" select="*:techservices" />
					</xsl:call-template>
					<xsl:apply-templates select="*:notes" />
				</repository>
			</repositories>
		</desc>
	</xsl:template>

	<xsl:template match="*:geogarea">
		<xsl:variable name="continents"
			select="('Africa', 'Asia', 'Australia', 'Europe', 'North America', 'South America')"
			as="xs:string+" />
		<geogarea>
			<xsl:choose>
				<xsl:when test="ape_static:contains(text(), $continents)">
					<xsl:value-of select="text()" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="'Europe'" />
				</xsl:otherwise>
			</xsl:choose>
		</geogarea>
	</xsl:template>

	<xsl:template match="*:country">
		<country>
			<xsl:if test="exists($xmlLang)">
				<xsl:attribute name="xml:lang" select="$xmlLang" />
			</xsl:if>
			<xsl:value-of select="text()" />
		</country>
	</xsl:template>

	<xsl:template match="*:firstdem">
		<xsl:if test="exists(text())">
			<firstdem>
				<xsl:if test="exists($xmlLang)">
					<xsl:attribute name="xml:lang" select="$xmlLang" />
				</xsl:if>
				<xsl:value-of select="text()" />
			</firstdem>
		</xsl:if>
	</xsl:template>

	<xsl:template match="*:secondem">
		<xsl:if test="exists(text())">
			<secondem>
				<xsl:if test="exists($xmlLang)">
					<xsl:attribute name="xml:lang" select="$xmlLang" />
				</xsl:if>
				<xsl:value-of select="text()" />
			</secondem>
		</xsl:if>
	</xsl:template>

	<xsl:function name="ape_static:contains" as="xs:boolean">
		<xsl:param name="str" as="xs:string" />
		<xsl:param name="list" as="xs:string+" />
		<xsl:variable name="temp" as="xs:boolean*">
			<xsl:for-each select="$list">
				<xsl:if test="contains($str, .)">
					<xsl:sequence select="xs:boolean('true')" />
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:sequence
			select="if ($temp[1] = xs:boolean('true')) then xs:boolean('true') else xs:boolean('false')" />
	</xsl:function>

	<xsl:template name="address">
		<xsl:param name="municipality" />
		<xsl:param name="street" />
		<xsl:param name="localentity" />
		<xsl:param name="postalcode" />
		<municipalityPostalcode>
			<xsl:if test="exists($xmlLang)">
				<xsl:attribute name="xml:lang" select="$xmlLang" />
			</xsl:if>
			<xsl:value-of select="concat($postalcode/text(), ' ', $municipality/text())" />
		</municipalityPostalcode>
		<xsl:if test="exists($localentity/text())">
			<localentity>
				<xsl:if test="exists($xmlLang)">
					<xsl:attribute name="xml:lang" select="$xmlLang" />
				</xsl:if>
				<xsl:value-of select="$localentity/text()" />
			</localentity>
		</xsl:if>
		<street>
			<xsl:if test="exists($xmlLang)">
				<xsl:attribute name="xml:lang" select="$xmlLang" />
			</xsl:if>
			<xsl:value-of select="$street/text()" />
		</street>
	</xsl:template>

	<xsl:template match="*:telephone">
		<telephone>
			<xsl:value-of select="text()" />
		</telephone>
	</xsl:template>

	<xsl:template match="*:fax">
		<xsl:if test="exists(text())">
			<fax>
				<xsl:value-of select="text()" />
			</fax>
		</xsl:if>
	</xsl:template>

	<xsl:template match="*:email">
		<email>
			<xsl:attribute name="href"
				select="if(@*:href) then @*:href else @href" />
			<xsl:value-of select="'Send an e-mail'" />
		</email>
	</xsl:template>

	<xsl:template match="*:webpage">
		<xsl:if test="exists(@*:href) or exists(@href)">
			<webpage>
				<xsl:attribute name="href"
					select="if(@*:href) then @*:href else @href" />
				<xsl:value-of select="'Go to the webpage'" />
			</webpage>
		</xsl:if>
	</xsl:template>

	<xsl:template match="*:repositorhist">
		<xsl:if test="exists(*:p/text())">
			<repositorhist>
				<descriptiveNote>
					<xsl:for-each select="*:p">
						<p>
							<xsl:if test="exists($xmlLang)">
								<xsl:attribute name="xml:lang" select="$xmlLang" />
							</xsl:if>
							<xsl:value-of select="text()" />
						</p>
					</xsl:for-each>
				</descriptiveNote>
			</repositorhist>
		</xsl:if>
	</xsl:template>

	<xsl:template match="*:repositorfound">
		<repositorfound>
			<date>
				<xsl:if test="exists($xmlLang)">
					<xsl:attribute name="xml:lang" select="$xmlLang" />
				</xsl:if>
				<xsl:attribute name="standardDate"
					select="ape:normalizeDate(normalize-space(*:date/@normal))" />
				<xsl:value-of select="*:date/@normal" />
			</date>
			<rule>
				<xsl:if test="exists($xmlLang)">
					<xsl:attribute name="xml:lang" select="$xmlLang" />
				</xsl:if>
				<xsl:value-of select="*:rule/text()" />
			</rule>
		</repositorfound>
	</xsl:template>

	<xsl:template match="*:repositorsup">
		<repositorsup>
			<date>
				<xsl:if test="exists($xmlLang)">
					<xsl:attribute name="xml:lang" select="$xmlLang" />
				</xsl:if>
				<xsl:attribute name="standardDate" select="*:date/@normal" />
				<xsl:value-of select="*:date/@normal" />
			</date>
			<rule>
				<xsl:if test="exists($xmlLang)">
					<xsl:attribute name="xml:lang" select="$xmlLang" />
				</xsl:if>
				<xsl:value-of select="*:rule/text()" />
			</rule>
		</repositorsup>
	</xsl:template>

	<xsl:template match="*:buildinginfo">
		<xsl:if test="exists(*:building or *:repositorarea or *:lengthshelf)">
			<buildinginfo>
				<xsl:if test="exists(*:building/*:p/text())">
					<building>
						<descriptiveNote>
							<xsl:for-each select="*:building/*:p">
								<p>
									<xsl:value-of select="text()" />
								</p>
							</xsl:for-each>
						</descriptiveNote>
					</building>
				</xsl:if>
				<xsl:if test="exists(*:repositorarea/*:num/text())">
					<repositorarea>
						<num>
							<xsl:attribute name="unit" select="'squaremetre'" />
							<xsl:value-of select="*:repositorarea/*:num/text()" />
						</num>
					</repositorarea>
				</xsl:if>
				<xsl:if test="exists(*:lengthshelf/*:num/text())">
					<lengthshelf>
						<num>
							<xsl:attribute name="unit" select="'linearmetre'" />
							<xsl:value-of select="*:lengthshelf/*:num/text()" />
						</num>
					</lengthshelf>
				</xsl:if>
			</buildinginfo>
		</xsl:if>
	</xsl:template>

	<xsl:template match="*:adminhierarchy">
		<xsl:if test="exists(*:adminunit/text())">
			<adminhierarchy>
				<xsl:for-each select="*:adminunit">
					<adminunit>
						<xsl:if test="exists($xmlLang)">
							<xsl:attribute name="xml:lang" select="$xmlLang" />
						</xsl:if>
						<xsl:value-of select="text()" />
					</adminunit>
				</xsl:for-each>
			</adminhierarchy>
		</xsl:if>
	</xsl:template>

	<xsl:template match="*:extent">
		<xsl:if test="exists(*:num/text())">
			<holdings>
				<extent>
					<num>
						<xsl:attribute name="unit" select="'linearmetre'" />
						<xsl:value-of select="*:num/text()" />
					</num>
				</extent>
			</holdings>
		</xsl:if>
	</xsl:template>

	<xsl:template match="*:timetable">
		<timetable>
			<opening>
				<xsl:value-of
					select="concat(*:opening/text(), ' - ', *:weekopen/*:num/text(), ' (', *:weekopen/*:num/@unit, ')')" />
			</opening>
			<xsl:if test="exists(*:closing/text())">
				<closing>
					<xsl:value-of select="*:closing/text()" />
				</closing>
			</xsl:if>
		</timetable>
	</xsl:template>

	<xsl:template match="*:access">
		<access>
			<xsl:attribute name="question" select="@question" />
			<xsl:if test="exists(*:restaccess/text())">
				<restaccess>
					<xsl:if test="exists($xmlLang)">
						<xsl:attribute name="xml:lang" select="$xmlLang" />
					</xsl:if>
					<xsl:value-of select="*:restaccess/text()" />
				</restaccess>
			</xsl:if>
		</access>
	</xsl:template>

	<xsl:template match="*:handicapped">
		<accessibility>
			<xsl:if test="exists($xmlLang)">
				<xsl:attribute name="xml:lang" select="$xmlLang" />
			</xsl:if>
			<xsl:attribute name="question" select="@question" />
		</accessibility>
	</xsl:template>

	<xsl:template name="services">
		<xsl:param name="searchroom" />
		<xsl:param name="techservices" />
		<services>
			<searchroom>
				<workPlaces>
					<num>
						<xsl:attribute name="unit" select="'site'" />
						<xsl:value-of select="$searchroom/*:num/text()" />
					</num>
				</workPlaces>
			</searchroom>
			<xsl:if
				test="exists($techservices/*:library/*:monographicpub or $techservices/*:library/*:serialpub)">
				<library question="no">
					<xsl:if test="$techservices/*:library/@question">
						<xsl:attribute name="question"
							select="$techservices/*:library/@question" />
					</xsl:if>
					<xsl:if test="exists($techservices/*:library/*:monographicpub)">
						<monographicpub>
							<num>
								<xsl:attribute name="unit"
									select="$techservices/*:library/*:monographicpub/*:num/@unit" />
								<xsl:value-of
									select="$techservices/*:library/*:monographicpub/*:num/text()" />
							</num>
						</monographicpub>
					</xsl:if>
					<xsl:if test="exists($techservices/*:library/*:serialpub)">
						<serialpub>
							<num>
								<xsl:attribute name="unit"
									select="if(ape_static:contains($techservices/*:library/*:serialpub/*:num/@unit, $unitList)) then $techservices/*:library/*:serialpub/*:num/@unit else 'title'" />
								<xsl:value-of select="$techservices/*:library/*:serialpub/*:num/text()" />
							</num>
						</serialpub>
					</xsl:if>
				</library>
			</xsl:if>

			<techservices>
				<xsl:if test="exists($techservices/*:restorationlab)">
					<restorationlab>
						<xsl:attribute name="question"
							select="$techservices/*:restorationlab/@question" />
					</restorationlab>
				</xsl:if>
				<reproductionser question="no">
					<xsl:variable name="reprod" select="$techservices/*:reproductionser" />
					<xsl:if test="$reprod/@question">
						<xsl:attribute name="question" select="$reprod/@question" />
					</xsl:if>
					<xsl:if test="exists($reprod/*:microformser)">
						<microformser>
							<xsl:attribute name="question"
								select="$reprod/*:microformser/@question" />
						</microformser>
					</xsl:if>
					<xsl:if test="exists($reprod/*:photographser)">
						<photographser>
							<xsl:attribute name="question"
								select="$reprod/*:photographser/@question" />
						</photographser>
					</xsl:if>
					<xsl:if test="exists($reprod/*:digitalser)">
						<digitalser>
							<xsl:attribute name="question"
								select="$reprod/*:digitalser/@question" />
						</digitalser>
					</xsl:if>
					<xsl:if test="exists($reprod/*:photocopyser)">
						<photocopyser>
							<xsl:attribute name="question"
								select="$reprod/*:photocopyser/@question" />
						</photocopyser>
					</xsl:if>
				</reproductionser>
			</techservices>
		</services>
	</xsl:template>

	<xsl:template match="*:notes">
		<xsl:if test="exists(*:p/text())">
			<descriptiveNote>
				<xsl:for-each select="*:p">
					<p>
						<xsl:if test="exists($xmlLang)">
							<xsl:attribute name="xml:lang" select="$xmlLang" />
						</xsl:if>
						<xsl:value-of select="text()" />
					</p>
				</xsl:for-each>
			</descriptiveNote>
		</xsl:if>
	</xsl:template>

	<xsl:template name="relations">
		<xsl:param name="repositorguides" />
		<relations>
			<xsl:for-each select="$repositorguides/*:repositorguide">
				<resourceRelation>
					<xsl:attribute name="resourceRelationType" select="'other'" />
					<xsl:attribute name="href" select="@href" />
					<relationEntry>
						<xsl:value-of select="text()" />
					</relationEntry>
				</resourceRelation>
			</xsl:for-each>
		</relations>
	</xsl:template>
</xsl:stylesheet>






















