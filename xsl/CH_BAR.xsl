<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="urn:isbn:1-931666-22-9"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:ape="http://www.archivesportaleurope.net/functions"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd"
                xpath-default-namespace="urn:isbn:1-931666-22-9"
                exclude-result-prefixes="xsl fo xs ape">

    <xsl:param name="loclanguage" select="'xsl/languages.xml'"/>
    <xsl:variable name="langfile" select="document($loclanguage)"/>

    <xsl:param name="eadiddefault" select="''"/>

    <xsl:param name="urlForIdentifiers" select="'https://www.swiss-archives.ch/detail.aspx?ID='"/>
    <xsl:param name="urlForDaosPre" select="'https://www.swiss-archives.ch/bild.aspx?VEID='"/>
    <xsl:param name="urlForDaosPost" select="'&amp;DEID=10&amp;SQNZNR=1'"/>
    <xsl:param name="urlForDaosThumbPre" select="'https://www.swiss-archives.ch/getimage.aspx?VEID='"/>
    <xsl:param name="urlForDaosThumbPost" select="'&amp;DEID=9&amp;SQNZNR=1&amp;SIZE=100'"/>

    <xsl:param name="langusage" select="''"/>
    <xsl:param name="langmaterial" select="''"/>
    <xsl:param name="addressline" select="''"/>
    <xsl:param name="publisher" select="''"/>
    <xsl:param name="author" select="''"/>
    <xsl:param name="repository" select="''"/>
    <xsl:param name="prefercite" select="''"/>
    <xsl:param name="mainagencycode" select="'CH-BAR'"/>
    <xsl:param name="countrycode" select="'CH'"/>
    <xsl:param name="versionnb" select="''"/>
    <xsl:param name="eadidmissing" select="''"/>
    <xsl:param name="useXSD10" select="'false'"/>
	
    <xsl:param name="url" select="''"/>
    <xsl:param name="provider" select="''"/>

    <xsl:output indent="yes" method="xml" />
    <xsl:strip-space elements="*"/>
    <!-- / -->

    <xsl:template match="/">
        <xsl:apply-templates select="node()" mode="top"/>
    </xsl:template>

    <!--#all: copy fonds intermediate lowest nested-->
    <!-- Elements unknown -->
    <xsl:template match="*" name="excludeElement" mode="#all">
        <xsl:variable name="excludedElement">
            <xsl:if test="name(../../../../../../../..) != ''">
                <xsl:value-of select="name(../../../../../../../..)"/><xsl:if test="name(../../../../../../../..)='c'">@<xsl:value-of select="../../../../../../../../@level" /></xsl:if><xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../../../../../..) != ''">
                <xsl:value-of select="name(../../../../../../..)"/><xsl:if test="name(../../../../../../..)='c'">@<xsl:value-of select="../../../../../../../@level" /></xsl:if><xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../../../../..) != ''">
                <xsl:value-of select="name(../../../../../..)"/><xsl:if test="name(../../../../../..)='c'">@<xsl:value-of select="../../../../../../@level" /></xsl:if><xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../../../..) != ''">
                <xsl:value-of select="name(../../../../..)"/><xsl:if test="name(../../../../..)='c'">@<xsl:value-of select="../../../../../@level" /></xsl:if><xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../../..) != ''">
                <xsl:value-of select="name(../../../..)"/><xsl:if test="name(../../../..)='c'">@<xsl:value-of select="../../../../@level" /></xsl:if><xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:if test="name(../../..) != ''">
                <xsl:value-of select="name(../../..)"/><xsl:if test="name(../../..)='c'">@<xsl:value-of select="../../../@level" /></xsl:if><xsl:text>/</xsl:text>
            </xsl:if>
            <xsl:value-of select="name(../..)"/><xsl:if test="name(../..)='c'">@<xsl:value-of select="../../@level" /></xsl:if><xsl:text>/</xsl:text><xsl:value-of select="name(..)"/><xsl:if test="name(..)='c'">@<xsl:value-of select="../@level" /></xsl:if><xsl:text>/</xsl:text><xsl:value-of select="name(.)"/><xsl:if test="name(.)='c'">@<xsl:value-of select="@level" /></xsl:if>
        </xsl:variable>
        <xsl:message select="normalize-space($excludedElement)" />
    </xsl:template>

    <!--
         copy all text nodes
         node() and text() have the same priority in XSLT 1.0 and 2.0
         to avoid "Recoverable error: XTRE0540: Ambiguous rule match for..."
         it is better to provide a priority
       -->
    <xsl:template match="text()" mode="#all" priority="2">
        <xsl:choose>
            <xsl:when test="contains(., '&#xa;')">
                <xsl:value-of select="translate(normalize-space(.), '&#xa;', ' ')"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- ead -->
    <xsl:template match="*:ExportRoot" name="ead" mode="top">
		<ead xmlns="urn:isbn:1-931666-22-9" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.archivesportaleurope.net/Portal/profiles/APEnet_EAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd" audience="external">
			<eadheader countryencoding="iso3166-1" dateencoding="iso8601" langencoding="iso639-2b" repositoryencoding="iso15511" scriptencoding="iso15924" relatedencoding="MARC21">
				<eadid>
					<xsl:if test="normalize-space($mainagencycode)">
						<xsl:attribute name="mainagencycode" select="$mainagencycode"/>
					</xsl:if>
					<xsl:if test="normalize-space($countrycode)">
						<xsl:attribute name="countrycode" select="$countrycode"/>
					</xsl:if>
					<xsl:attribute name="identifier">
						<xsl:value-of select="$countrycode"/>_<xsl:value-of select="$mainagencycode"/>
					</xsl:attribute>
					<xsl:value-of select="*:Record/*:DetailData/*:DataElement[@ElementId='2']/*:ElementValue/*:TextValue/text()"/>
				</eadid>
				<filedesc>
					<titlestmt>
						<titleproper>
                            <xsl:value-of select="*:Record/*:DetailData/*:DataElement[@ElementId='1']/*:ElementValue/*:TextValue/text()"/>
						</titleproper>
						<author>
                            <xsl:for-each select="*:Record/*:DetailData/*:DataElement[@ElementId='14']/*:ElementValue">
                                <xsl:value-of select="*:TextValue/text()"/>
                            </xsl:for-each>
						</author>
					</titlestmt>
				</filedesc>
				<revisiondesc>
					<change>
						<date />
						<item>Converted_APEnet_EAD_version_<xsl:value-of select="$versionnb"/></item>
					</change>
				</revisiondesc>
			</eadheader>
			<archdesc level="fonds">
				<did>
					<unitid type="call number">
						<xsl:value-of select="*:Record/*:DetailData/*:DataElement[@ElementId='2']/*:ElementValue/*:TextValue/text()"/>
					</unitid>
					<unittitle>
                        <xsl:value-of select="*:Record/*:DetailData/*:DataElement[@ElementId='1']/*:ElementValue/*:TextValue/text()"/>
					</unittitle>
					<unitdate>
                        <xsl:attribute name="encodinganalog" select="'3.1.3'"/>
                        <xsl:attribute name="era" select="'ce'"/>
                        <xsl:attribute name="calendar" select="'gregorian'"/>
                        <xsl:variable name="normal">
                            <xsl:value-of select="ape:normalizeDate(normalize-space(*:Record/*:DetailData/*:DataElement[@ElementId='7']/*:ElementValue/*:DateRange/*:TextRepresentation/text()))"/>
                        </xsl:variable>
                        <xsl:if test="normalize-space($normal)">
                            <xsl:attribute name="normal" select="$normal"/>
                        </xsl:if>
                        <xsl:value-of select="*:Record/*:DetailData/*:DataElement[@ElementId='7']/*:ElementValue/*:DateRange/*:TextRepresentation/text()"/>
					</unitdate>
					<!--<xsl:variable name="record_provenance_cnt" select="count(*:Record/*:DetailData/*:DataElement[@ElementId='10032']/*:ElementValue/*:TextValue/text())"/>								-->
					<!--<xsl:if test="$record_provenance_cnt > 0">-->
						<!--<xsl:for-each select="*:Record/*:DetailData/*:DataElement[@ElementId='10032']/*:ElementValue">-->
							<!--<xsl:choose>-->
								<!--<xsl:when test="position() = last()">-->
									<!--<origination label="final">-->
										<!--<xsl:value-of select="*:TextValue/text()"/>-->
									<!--</origination>-->
								<!--</xsl:when>-->
								<!--<xsl:otherwise>-->
									<!--<origination label="pre">-->
										<!--<xsl:value-of select="*:TextValue/text()"/>-->
									<!--</origination>-->
								<!--</xsl:otherwise>-->
							<!--</xsl:choose>									-->
						<!--</xsl:for-each>-->
					<!--</xsl:if>-->
					<!--<xsl:variable name="record_language_cnt" select="count(*:Record/*:DetailData/*:DataElement[@ElementId='10041']/*:ElementValue/*:TextValue/text())"/>				-->
					<!--<xsl:if test="$record_language_cnt > 0">-->
						<!--<langmaterial>-->
							<!--<xsl:for-each select="*:Record/*:DetailData/*:DataElement[@ElementId='10041']/*:ElementValue">-->
								<!--<language>-->
									<!--<xsl:value-of select="*:TextValue/text()"/>-->
								<!--</language>-->
							<!--</xsl:for-each>				-->
						<!--</langmaterial>-->
					<!--</xsl:if>-->
					<!--<xsl:variable name="record_extent_units" select="*:Record/*:DetailData/*:DataElement[@ElementId='10111']/*:ElementValue/*:TextValue/text()"/>-->
					<!--<xsl:variable name="record_extent_box" select="*:Record/*:DetailData/*:DataElement[@ElementId='23']/*:ElementValue/*:TextValue/text()"/>-->
					<!--<xsl:variable name="record_extent" select="*:Record/*:DetailData/*:DataElement[@ElementId='24']/*:ElementValue/*:TextValue/text()"/>-->
					<!--<xsl:variable name="record_extent_meters" select="*:Record/*:DetailData/*:DataElement[@ElementId='25']/*:ElementValue/*:TextValue/text()"/>-->
					<!--<xsl:variable name="record_type_of_archival_material_cnt" select="count(*:Record/*:DetailData/*:DataElement[@ElementId='17']/*:ElementValue/*:TextValue/text())"/>-->
					<!--<xsl:variable name="record_format_cnt" select="count(*:Record/*:DetailData/*:DataElement[@ElementId='10384']/*:ElementValue/*:TextValue/text())"/>-->
					<!--<xsl:if test="$record_extent_units or $record_extent_box or $record_extent or $record_extent_meters or $record_type_of_archival_material_cnt > 0 or $record_format_cnt > 0">-->
						<!--<physdesc>-->
							<!--<xsl:if test="$record_extent_units">-->
								<!--<extent unit="units"><xsl:value-of select="$record_extent_units"/></extent>-->
							<!--</xsl:if>		-->
							<!--<xsl:if test="$record_extent_box">-->
								<!--<extent unit="box"><xsl:value-of select="$record_extent_box"/></extent>-->
							<!--</xsl:if>-->
                            <!--<xsl:if test="$record_extent">-->
                                <!--<extent><xsl:value-of select="$record_extent"/></extent>-->
                            <!--</xsl:if>-->
							<!--<xsl:if test="$record_extent_meters">-->
								<!--<extent unit="meters"><xsl:value-of select="$record_extent_meters"/></extent>-->
							<!--</xsl:if>		-->
							<!--<xsl:if test="$record_type_of_archival_material_cnt > 0">-->
								<!--<xsl:for-each select="*:Record/*:DetailData/*:DataElement[@ElementId='17']/*:ElementValue">-->
									<!--<genreform>-->
										<!--<xsl:value-of select="*:TextValue/text()"/>-->
									<!--</genreform>-->
								<!--</xsl:for-each>								-->
							<!--</xsl:if>-->
                            <!--<xsl:if test="$record_format_cnt > 0">-->
                                <!--<xsl:for-each select="*:Record/*:DetailData/*:DataElement[@ElementId='10384']/*:ElementValue">-->
                                    <!--<genreform>-->
                                        <!--<xsl:value-of select="*:TextValue/text()"/>-->
                                    <!--</genreform>-->
                                <!--</xsl:for-each>-->
                            <!--</xsl:if>-->
						<!--</physdesc>				-->
					<!--</xsl:if>-->
                    <xsl:variable name="record_format_cnt" select="count(*:Record/*:DetailData/*:DataElement[@ElementId='10151']/*:ElementValue/*:TextValue/text())"/>
                    <xsl:if test="$record_format_cnt > 0">
                        <physdesc>
                            <xsl:for-each select="*:Record/*:DetailData/*:DataElement[@ElementId='10384']/*:ElementValue">
                                <genreform>
                                    <xsl:value-of select="*:TextValue/text()"/>
                                </genreform>
                            </xsl:for-each>
                        </physdesc>
                    </xsl:if>
                    <!--<xsl:variable name="record_aufnahmeart" select="*:Record/*:DetailData/*:DataElement[@ElementId='10197']/*:ElementValue/*:TextValue/text()"/>-->
                    <!--<xsl:if test="$record_aufnahmeart">-->
                        <!--<materialspec>-->
                            <!--<xsl:value-of select="$record_aufnahmeart"/>-->
                        <!--</materialspec>-->
                    <!--</xsl:if>-->
				</did>
				<!--<xsl:variable name="record_archival_history" select="*:Record/*:DetailData/*:DataElement[@ElementId='10010']/*:ElementValue/*:TextValue/text()"/>-->
				<!--<xsl:if test="$record_archival_history">-->
					<!--<custodhist>-->
						<!--<p><xsl:value-of select="$record_archival_history"/></p>-->
					<!--</custodhist>-->
				<!--</xsl:if>-->
                <xsl:variable name="record_archival_history" select="*:Record/*:DetailData/*:DataElement[@ElementId='505']/*:ElementValue/*:TextValue/text()"/>
                <xsl:if test="$record_archival_history">
                    <custodhist>
                        <p><xsl:value-of select="$record_archival_history"/></p>
                    </custodhist>
                </xsl:if>
				<!--<xsl:variable name="record_creator_existence" select="*:Record/*:DetailData/*:DataElement[@ElementId='10111']/*:ElementValue/*:TextValue/text()"/>-->
				<!--<xsl:variable name="record_administrative_history" select="*:Record/*:DetailData/*:DataElement[@ElementId='10009']/*:ElementValue/*:TextValue/text()"/>-->
				<!--<xsl:if test="$record_creator_existence or $record_administrative_history">				-->
					<!--<bioghist>-->
						<!--<xsl:if test="$record_creator_existence">				-->
							<!--<p><xsl:value-of select="$record_creator_existence"/></p>-->
						<!--</xsl:if>		-->
						<!--<xsl:if test="$record_administrative_history">				-->
							<!--<p><xsl:value-of select="$record_administrative_history"/></p>-->
						<!--</xsl:if>-->
					<!--</bioghist>-->
				<!--</xsl:if>							-->
				<!--<xsl:variable name="record_oredering_and_classification" select="*:Record/*:DetailData/*:DataElement[@ElementId='10037']/*:ElementValue/*:TextValue/text()"/>						-->
				<!--<xsl:if test="$record_oredering_and_classification">-->
					<!--<arrangement>-->
						<!--<p><xsl:value-of select="$record_oredering_and_classification"/></p>-->
					<!--</arrangement>-->
				<!--</xsl:if>-->
                <xsl:variable name="record_ordering_and_classification" select="*:Record/*:DetailData/*:DataElement[@ElementId='10239']/*:ElementValue/*:TextValue/text()"/>
                <xsl:if test="$record_ordering_and_classification">
                    <arrangement>
                        <p><xsl:value-of select="$record_ordering_and_classification"/></p>
                    </arrangement>
                </xsl:if>
				<!--<xsl:variable name="record_description_of_contents" select="*:Record/*:DetailData/*:DataElement[@ElementId='10034']/*:ElementValue/*:TextValue/text()"/>-->
				<!--<xsl:variable name="record_description_of_contents_also" select="*:Record/*:DetailData/*:DataElement[@ElementId='8']/*:ElementValue/*:TextValue/text()"/>-->
                <!--<xsl:variable name="record_description_of_contents_urheber" select="*:Record/*:DetailData/*:DataElement[@ElementId='10376']/*:ElementValue/*:TextValue/text()"/>-->
                <!--<xsl:variable name="record_description_of_contents_land" select="*:Record/*:DetailData/*:DataElement[@ElementId='10174']/*:ElementValue/*:TextValue/text()"/>-->
                <!--<xsl:variable name="record_description_of_contents_betailigte_personen" select="*:Record/*:DetailData/*:DataElement[@ElementId='10377']/*:ElementValue/*:TextValue/text()"/>-->
                <!--<xsl:variable name="record_description_of_contents_verleger" select="*:Record/*:DetailData/*:DataElement[@ElementId='10378']/*:ElementValue/*:TextValue/text()"/>-->
                <!--<xsl:variable name="record_description_of_contents_abdeckung" select="*:Record/*:DetailData/*:DataElement[@ElementId='10379']/*:ElementValue/*:TextValue/text()"/>-->
                <!--<xsl:variable name="record_description_of_contents_thema" select="*:Record/*:DetailData/*:DataElement[@ElementId='10380']/*:ElementValue/*:TextValue/text()"/>-->
                <!--<xsl:if test="$record_description_of_contents or $record_description_of_contents_also or $record_description_of_contents_urheber or $record_description_of_contents_land or $record_description_of_contents_betailigte_personen or $record_description_of_contents_verleger or $record_description_of_contents_abdeckung or $record_description_of_contents_thema">-->
					<!--<scopecontent>-->
						<!--<xsl:if test="$record_description_of_contents">-->
						    <!--<p><xsl:value-of select="$record_description_of_contents"/></p>-->
						<!--</xsl:if>-->
						<!--<xsl:if test="$record_description_of_contents_also">-->
						    <!--<p><xsl:value-of select="$record_description_of_contents_also"/></p>-->
						<!--</xsl:if>-->
                        <!--<xsl:if test="$record_description_of_contents_urheber">-->
                            <!--<p><xsl:value-of select="$record_description_of_contents_urheber"/></p>-->
                        <!--</xsl:if>-->
                        <!--<xsl:if test="$record_description_of_contents_land">-->
                            <!--<p><xsl:value-of select="$record_description_of_contents_land"/></p>-->
                        <!--</xsl:if>-->
                        <!--<xsl:if test="$record_description_of_contents_betailigte_personen">-->
                            <!--<p><xsl:value-of select="$record_description_of_contents_betailigte_personen"/></p>-->
                        <!--</xsl:if>-->
                        <!--<xsl:if test="$record_description_of_contents_verleger">-->
                            <!--<p><xsl:value-of select="$record_description_of_contents_verleger"/></p>-->
                        <!--</xsl:if>-->
                        <!--<xsl:if test="$record_description_of_contents_abdeckung">-->
                            <!--<p><xsl:value-of select="$record_description_of_contents_abdeckung"/></p>-->
                        <!--</xsl:if>-->
                        <!--<xsl:if test="$record_description_of_contents_thema">-->
                            <!--<p><xsl:value-of select="$record_description_of_contents_thema"/></p>-->
                        <!--</xsl:if>-->
					<!--</scopecontent>-->
				<!--</xsl:if>-->
                <xsl:variable name="record_description_of_contents" select="*:Record/*:DetailData/*:DataElement[@ElementId='8']/*:ElementValue/*:TextValue/text()"/>
                <xsl:if test="$record_description_of_contents">
                    <scopecontent>
                        <p><xsl:value-of select="$record_description_of_contents"/></p>
                    </scopecontent>
                </xsl:if>
				<dsc type="othertype">
					<xsl:apply-templates select="node()" mode="copy"/>
				</dsc>
			</archdesc>
		</ead>
    </xsl:template>

    <!-- c -->
    <xsl:template match="*:Record" mode="copy">
        <c>
            <xsl:variable name="myLevel">
                <xsl:if test="@Level='Hauptabteilung'">fonds</xsl:if>
                <xsl:if test="@Level='Bestand'">fonds</xsl:if>
                <xsl:if test="@Level='Teilbestand'">fonds</xsl:if>
                <xsl:if test="@Level='Serie'">series</xsl:if>
                <xsl:if test="@Level='Dossier'">file</xsl:if>
                <xsl:if test="@Level='Subdossier'">file</xsl:if>
		        <xsl:if test="@Level='Dokument'">item</xsl:if>
            </xsl:variable>
            <xsl:attribute name="level" select="$myLevel"/>
            <did>
                <unitid type="call number">
                    <xsl:value-of select="*:DetailData/*:DataElement[@ElementId='2']/*:ElementValue/*:TextValue/text()"/>
                    <extptr><xsl:attribute name="xlink:href" select="concat($urlForIdentifiers, @Id)"/></extptr>
                </unitid>
				<unittitle>
                    <xsl:value-of select="*:DetailData/*:DataElement[@ElementId='1']/*:ElementValue/*:TextValue/text()"/>
				</unittitle>
				<unitdate>
                    <xsl:attribute name="encodinganalog" select="'3.1.3'"/>
                    <xsl:attribute name="era" select="'ce'"/>
                    <xsl:attribute name="calendar" select="'gregorian'"/>
                    <xsl:variable name="normal">
                        <xsl:value-of select="ape:normalizeDate(normalize-space(*:DetailData/*:DataElement[@ElementId='7']/*:ElementValue/*:DateRange/*:TextRepresentation/text()))"/>
                    </xsl:variable>
                    <xsl:if test="normalize-space($normal)">
                        <xsl:attribute name="normal" select="$normal"/>
                    </xsl:if>
                    <xsl:value-of select="*:DetailData/*:DataElement[@ElementId='7']/*:ElementValue/*:DateRange/*:TextRepresentation/text()"/>
				</unitdate>				
				<!--<xsl:variable name="detail_provenance_cnt" select="count(*:DetailData/*:DataElement[@ElementId='10032']/*:ElementValue/*:TextValue/text())"/>								-->
				<!--<xsl:if test="$detail_provenance_cnt > 0">-->
					<!--<xsl:for-each select="*:DetailData/*:DataElement[@ElementId='10032']/*:ElementValue">-->
						<!--<xsl:choose>-->
							<!--<xsl:when test="position() = last()">-->
								<!--<origination label="final">-->
									<!--<xsl:value-of select="*:TextValue/text()"/>-->
								<!--</origination>-->
							<!--</xsl:when>-->
							<!--<xsl:otherwise>-->
								<!--<origination label="pre">-->
									<!--<xsl:value-of select="*:TextValue/text()"/>-->
								<!--</origination>-->
							<!--</xsl:otherwise>-->
						<!--</xsl:choose>									-->
					<!--</xsl:for-each>-->
				<!--</xsl:if>-->
				<!--<xsl:variable name="detail_language_cnt" select="count(*:DetailData/*:DataElement[@ElementId='10041']/*:ElementValue/*:TextValue/text())"/>				-->
				<!--<xsl:if test="$detail_language_cnt > 0">-->
					<!--<langmaterial>-->
						<!--<xsl:for-each select="*:DetailData/*:DataElement[@ElementId='10041']/*:ElementValue">-->
							<!--<language>-->
								<!--<xsl:value-of select="*:TextValue/text()"/>-->
							<!--</language>-->
						<!--</xsl:for-each>				-->
					<!--</langmaterial>-->
				<!--</xsl:if>					-->
				<!--<xsl:variable name="detail_extent_units" select="*:DetailData/*:DataElement[@ElementId='10111']/*:ElementValue/*:TextValue/text()"/>-->
				<!--<xsl:variable name="detail_extent_box" select="*:DetailData/*:DataElement[@ElementId='24']/*:ElementValue/*:TextValue/text()"/>-->
                <!--<xsl:variable name="detail_extent" select="*:DetailData/*:DataElement[@ElementId='24']/*:ElementValue/*:TextValue/text()"/>-->
				<!--<xsl:variable name="detail_extent_meters" select="*:DetailData/*:DataElement[@ElementId='25']/*:ElementValue/*:TextValue/text()"/>-->
				<!--<xsl:variable name="detail_type_of_archival_material_cnt" select="count(*:DetailData/*:DataElement[@ElementId='17']/*:ElementValue/*:TextValue/text())"/>-->
				<!--<xsl:variable name="detail_format_cnt" select="count(*:DetailData/*:DataElement[@ElementId='10384']/*:ElementValue/*:TextValue/text())"/>-->
				<!--<xsl:if test="$detail_extent_units or $detail_extent_box or $detail_extent or $detail_extent_meters or $detail_type_of_archival_material_cnt > 0 or $detail_format_cnt > 0">-->
					<!--<physdesc>-->
						<!--<xsl:if test="$detail_extent_units">-->
							<!--<extent unit="units"><xsl:value-of select="$detail_extent_units"/></extent>-->
						<!--</xsl:if>		-->
						<!--<xsl:if test="$detail_extent_box">-->
							<!--<extent unit="box"><xsl:value-of select="$detail_extent_box"/></extent>-->
						<!--</xsl:if>-->
                        <!--<xsl:if test="$detail_extent">-->
                            <!--<extent><xsl:value-of select="$detail_extent"/></extent>-->
                        <!--</xsl:if>-->
						<!--<xsl:if test="$detail_extent_meters">-->
							<!--<extent unit="meters"><xsl:value-of select="$detail_extent_meters"/></extent>-->
						<!--</xsl:if>		-->
						<!--<xsl:if test="$detail_type_of_archival_material_cnt > 0">-->
							<!--<xsl:for-each select="*:DetailData/*:DataElement[@ElementId='17']/*:ElementValue">-->
								<!--<genreform>-->
									<!--<xsl:value-of select="*:TextValue/text()"/>-->
								<!--</genreform>-->
							<!--</xsl:for-each>								-->
						<!--</xsl:if>-->
                        <!--<xsl:if test="$detail_format_cnt > 0">-->
                            <!--<xsl:for-each select="*:DetailData/*:DataElement[@ElementId='10384']/*:ElementValue">-->
                                <!--<genreform>-->
                                    <!--<xsl:value-of select="*:TextValue/text()"/>-->
                                <!--</genreform>-->
                            <!--</xsl:for-each>-->
                        <!--</xsl:if>-->
					<!--</physdesc>				-->
				<!--</xsl:if>-->
                <xsl:variable name="detail_format_cnt" select="count(*:DetailData/*:DataElement[@ElementId='10151']/*:ElementValue/*:TextValue/text())"/>
                <xsl:if test="$detail_format_cnt > 0">
                    <physdesc>
                        <xsl:for-each select="*:DetailData/*:DataElement[@ElementId='10384']/*:ElementValue">
                            <genreform>
                                <xsl:value-of select="*:TextValue/text()"/>
                            </genreform>
                        </xsl:for-each>
                    </physdesc>
                </xsl:if>
                <!--<xsl:variable name="detail_aufnahmeart" select="*:DetailData/*:DataElement[@ElementId='10197']/*:ElementValue/*:TextValue/text()"/>-->
                <!--<xsl:if test="$detail_aufnahmeart">-->
                    <!--<materialspec>-->
                        <!--<xsl:value-of select="$detail_aufnahmeart"/>-->
                    <!--</materialspec>-->
                <!--</xsl:if>-->
                <xsl:if test="$myLevel = 'item' and /*:ExportRoot/*:Record/*:DetailData/*:DataElement[@ElementId='2']/*:ElementValue/*:TextValue/text() = 'E27*'">
                    <dao>
                        <xsl:attribute name="xlink:href" select="concat(concat($urlForDaosPre, @Id), $urlForDaosPost)"/>
                        <xsl:attribute name="xlink:title" select="'Link to original'"/>
                        <xsl:attribute name="xlink:role" select="'UNSPECIFIED'"/>
                    </dao>
                    <dao>
                        <xsl:attribute name="xlink:href" select="concat(concat($urlForDaosThumbPre, @Id), $urlForDaosThumbPost)"/>
                        <xsl:attribute name="xlink:title" select="'thumbnail'"/>
                        <xsl:attribute name="xlink:role" select="'thumbnail'"/>
                    </dao>
                </xsl:if>
            </did>
			<!--<xsl:variable name="detail_archival_history" select="*:DetailData/*:DataElement[@ElementId='10010']/*:ElementValue/*:TextValue/text()"/>-->
			<!--<xsl:if test="$detail_archival_history">-->
				<!--<custodhist>-->
					<!--<p><xsl:value-of select="$detail_archival_history"/></p>-->
				<!--</custodhist>-->
			<!--</xsl:if>-->
            <xsl:variable name="detail_archival_history" select="*:DetailData/*:DataElement[@ElementId='505']/*:ElementValue/*:TextValue/text()"/>
            <xsl:if test="$detail_archival_history">
                <custodhist>
                    <p><xsl:value-of select="$detail_archival_history"/></p>
                </custodhist>
            </xsl:if>
			<!--<xsl:variable name="detail_creator_existence" select="*:DetailData/*:DataElement[@ElementId='10111']/*:ElementValue/*:TextValue/text()"/>-->
			<!--<xsl:variable name="detail_administrative_history" select="*:DetailData/*:DataElement[@ElementId='10009']/*:ElementValue/*:TextValue/text()"/>-->
			<!--<xsl:if test="$detail_creator_existence or $detail_administrative_history">				-->
				<!--<bioghist>-->
					<!--<xsl:if test="$detail_creator_existence">				-->
						<!--<p><xsl:value-of select="$detail_creator_existence"/></p>-->
					<!--</xsl:if>		-->
					<!--<xsl:if test="$detail_administrative_history">				-->
						<!--<p><xsl:value-of select="$detail_administrative_history"/></p>-->
					<!--</xsl:if>-->
				<!--</bioghist>-->
			<!--</xsl:if>-->
			<!--<xsl:variable name="detail_oredering_and_classification" select="*:DetailData/*:DataElement[@ElementId='10037']/*:ElementValue/*:TextValue/text()"/>						-->
			<!--<xsl:if test="$detail_oredering_and_classification">-->
				<!--<arrangement>-->
					<!--<p><xsl:value-of select="$detail_oredering_and_classification"/></p>-->
				<!--</arrangement>-->
			<!--</xsl:if>		-->
            <xsl:variable name="detail_oredering_and_classification" select="*:DetailData/*:DataElement[@ElementId='10239']/*:ElementValue/*:TextValue/text()"/>
            <xsl:if test="$detail_oredering_and_classification and $myLevel = 'fonds'">
                <arrangement>
                    <p><xsl:value-of select="$detail_oredering_and_classification"/></p>
                </arrangement>
            </xsl:if>
			<!--<xsl:variable name="detail_description_of_contents" select="*:DetailData/*:DataElement[@ElementId='10034']/*:ElementValue/*:TextValue/text()"/>-->
			<!--<xsl:variable name="detail_description_of_contents_also" select="*:DetailData/*:DataElement[@ElementId='8']/*:ElementValue/*:TextValue/text()"/>-->
			<!--<xsl:variable name="detail_description_of_contents_urheber" select="*:DetailData/*:DataElement[@ElementId='10376']/*:ElementValue/*:TextValue/text()"/>-->
			<!--<xsl:variable name="detail_description_of_contents_land" select="*:DetailData/*:DataElement[@ElementId='10174']/*:ElementValue/*:TextValue/text()"/>-->
			<!--<xsl:variable name="detail_description_of_contents_betailigte_personen" select="*:DetailData/*:DataElement[@ElementId='10377']/*:ElementValue/*:TextValue/text()"/>-->
			<!--<xsl:variable name="detail_description_of_contents_verleger" select="*:DetailData/*:DataElement[@ElementId='10378']/*:ElementValue/*:TextValue/text()"/>-->
			<!--<xsl:variable name="detail_description_of_contents_abdeckung" select="*:DetailData/*:DataElement[@ElementId='10379']/*:ElementValue/*:TextValue/text()"/>-->
			<!--<xsl:variable name="detail_description_of_contents_thema" select="*:DetailData/*:DataElement[@ElementId='10380']/*:ElementValue/*:TextValue/text()"/>-->
			<!--<xsl:if test="$detail_description_of_contents or $detail_description_of_contents_also or $detail_description_of_contents_urheber or $detail_description_of_contents_land or $detail_description_of_contents_betailigte_personen or $detail_description_of_contents_verleger or $detail_description_of_contents_abdeckung or $detail_description_of_contents_thema">-->
				<!--<scopecontent>-->
					<!--<xsl:if test="$detail_description_of_contents">-->
						<!--<p><xsl:value-of select="$detail_description_of_contents"/></p>-->
					<!--</xsl:if>-->
					<!--<xsl:if test="$detail_description_of_contents_also">-->
                        <!--<p><xsl:value-of select="$detail_description_of_contents_also"/></p>-->
					<!--</xsl:if>-->
                    <!--<xsl:if test="$detail_description_of_contents_urheber">-->
                        <!--<p><xsl:value-of select="$detail_description_of_contents_urheber"/></p>-->
                    <!--</xsl:if>-->
                    <!--<xsl:if test="$detail_description_of_contents_land">-->
                        <!--<p><xsl:value-of select="$detail_description_of_contents_land"/></p>-->
                    <!--</xsl:if>-->
                    <!--<xsl:if test="$detail_description_of_contents_betailigte_personen">-->
                        <!--<p><xsl:value-of select="$detail_description_of_contents_betailigte_personen"/></p>-->
                    <!--</xsl:if>-->
                    <!--<xsl:if test="$detail_description_of_contents_verleger">-->
                        <!--<p><xsl:value-of select="$detail_description_of_contents_verleger"/></p>-->
                    <!--</xsl:if>-->
                    <!--<xsl:if test="$detail_description_of_contents_abdeckung">-->
                        <!--<p><xsl:value-of select="$detail_description_of_contents_abdeckung"/></p>-->
                    <!--</xsl:if>-->
                    <!--<xsl:if test="$detail_description_of_contents_thema">-->
                        <!--<p><xsl:value-of select="$detail_description_of_contents_thema"/></p>-->
                    <!--</xsl:if>-->
				<!--</scopecontent>-->
			<!--</xsl:if>-->
            <xsl:variable name="detail_description_of_contents" select="*:DetailData/*:DataElement[@ElementId='8']/*:ElementValue/*:TextValue/text()"/>
            <xsl:if test="$detail_description_of_contents">
                <scopecontent>
                    <p><xsl:value-of select="$detail_description_of_contents"/></p>
                </scopecontent>
            </xsl:if>

            <xsl:variable name="detail_controlaccess" select="count(*:DetailData/*:DataElement[@ElementId='10234']/*:ElementValue/*:TextValue/text())" />
            <xsl:if test="$detail_controlaccess > 0">
                <controlaccess>
                    <xsl:for-each select="*:DetailData/*:DataElement[@ElementId='10234']">
                        <name>
                            <xsl:value-of select="*:ElementValue/*:TextValue/text()" />
                        </name>
                    </xsl:for-each>
                </controlaccess>
            </xsl:if>

            <xsl:apply-templates mode="copy"/> 
        </c>
	</xsl:template>

</xsl:stylesheet>
