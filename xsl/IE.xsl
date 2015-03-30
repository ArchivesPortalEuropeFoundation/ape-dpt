<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="urn:isbn:1-931666-22-9"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:none="none"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:ape="http://www.archivesportaleurope.net/functions"
                xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd"
                xpath-default-namespace="urn:isbn:1-931666-22-9"
                exclude-result-prefixes="xsl fo xs none ape">
    <!---->

    <!--xsl:import href="parameter.xsl" /-->
    <xsl:param name="loclanguage" select="'xsl/languages.xml'"/>
    <xsl:variable name="langfile" select="document($loclanguage)"/>

    <!-- Parameterfenster (parameterDef.xml) -->
    <xsl:param name="langusage" select="''"/>
    <xsl:param name="langmaterial" select="''"/>
    <xsl:param name="addressline" select="''"/>
    <xsl:param name="publisher" select="''"/>
    <xsl:param name="author" select="''"/>
    <xsl:param name="repository" select="''"/>
    <xsl:param name="prefercite" select="''"/>
    <xsl:param name="countrycode" select="''"/>
    <xsl:param name="mainagencycode" select="''"/>
    <xsl:param name="eadidmissing" select="''"/>
    <xsl:param name="useXSD10" select="'false'"/>

    <xsl:param name="url" select="''"/>
    <xsl:param name="provider" select="''"/>

    <xsl:output indent="yes" method="xml" />
    <xsl:strip-space elements="*"/>
    <!-- / -->

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
        <xsl:message><xsl:value-of select="normalize-space($excludedElement)" /></xsl:message>
    </xsl:template>

    <xsl:template match="/">
        <xsl:choose>
            <xsl:when test="//eadid[@countrycode]">
                <xsl:message>The countrycode is: <xsl:value-of select="//eadid/@countrycode"/></xsl:message>
            </xsl:when>
            <xsl:otherwise>
                <xsl:message>No countrycode, we will use default transformation.</xsl:message>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:message>Element excluded from the transformation (if any):</xsl:message>
        <xsl:apply-templates select="node()" mode="top"/>
    </xsl:template>

    <xsl:template match="text()" mode="#all" priority="2">
        <xsl:choose>
            <xsl:when test="contains(., '&#xa;')">
                <xsl:value-of select="translate(normalize-space(.), '&#xa;', ' ')"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="preceding-sibling::node()[1][self::node()]"><xsl:text> </xsl:text></xsl:if><xsl:value-of select="normalize-space(.)"/><xsl:if test="following-sibling::node()[1][self::node()]"><xsl:text> </xsl:text></xsl:if>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="*[name()='adlibXML']" mode="top">
        <ead xmlns="urn:isbn:1-931666-22-9"
            xmlns:xlink="http://www.w3.org/1999/xlink"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.archivesportaleurope.net/Portal/profiles/apeEAD.xsd http://www.w3.org/1999/xlink http://www.loc.gov/standards/xlink/xlink.xsd" audience="external">
            <xsl:apply-templates select="node()" mode="copy"/>
        </ead>
    </xsl:template>

    <xsl:template match="*[name()='recordList']" mode="copy">
        <eadheader>
            <eadid>
				<xsl:if test="normalize-space($countrycode)">
		            <xsl:attribute name="countrycode" select="$countrycode"/>
		        </xsl:if>
		        <xsl:if test="normalize-space($mainagencycode)">
		            <xsl:attribute name="mainagencycode" select="$mainagencycode"/>
		        </xsl:if>
                <xsl:attribute name="identifier">
                    <xsl:value-of select="$mainagencycode"/><xsl:text>_</xsl:text><xsl:value-of select="$eadidmissing"/>
                </xsl:attribute>
                <xsl:value-of select="$eadidmissing"/>
			</eadid>
            <filedesc>
                <titlestmt>
                    <titleproper>
                        <xsl:value-of select="child::*[1][name()='record']/*[name()='title']/text()"/>
                    </titleproper>
                </titlestmt>
            </filedesc>
            <xsl:if test="descendant::*[name()='rules_convention'][1]">
                <profiledesc>
                    <descrules>
                        <p>
                            <xsl:value-of select="descendant::*[name()='rules_convention'][1]/text()"/>
                        </p>
                    </descrules>
                </profiledesc>
            </xsl:if>
        </eadheader>
        <archdesc level="fonds">
            <did>
                <xsl:apply-templates select="child::*[1][name()='record']/*[not(name()='content.description' or name()='rights.notes' or name()='access_category.notes')]" mode="copy"/>
            </did>
            <xsl:apply-templates select="child::*[1][name()='record']/*[name()='content.description' or name()='rights.notes' or name()='access_category.notes']" mode="copy"/>
            <xsl:if test="child::*[1][name()='record']/*[name()='content.subject']">
                <controlaccess>
                    <xsl:for-each select="child::*[1][name()='record']/*[name()='content.subject']">
                        <xsl:variable name="pos" select="position()"/>
                        <xsl:choose>
                            <xsl:when test="(../*[name()='content.subject.type'][position()=$pos]/@option = 'PLACE') or (../*[name()='content.subject.type'][position()=$pos]/@value = 'PLACE')">
                                <geogname>
                                    <xsl:value-of select="./text()"/>
                                </geogname>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:call-template name="excludeElement" />
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:for-each>
                    <xsl:for-each select="child::*[1][name()='record']/*[name()='content.person.name']">
                        <xsl:variable name="pos" select="position()"/>
                        <xsl:choose>
                            <xsl:when test="../*[name()='content.person.name.type'][position()=$pos]/@option = 'PERSON'">
                                <persname>
                                    <xsl:value-of select="./text()"/>
                                </persname>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:call-template name="excludeElement" />
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:for-each>
                </controlaccess>
            </xsl:if>
            <bibliography>
                <p>
                    <xsl:value-of select="child::*[1][name()='record']/*[name()='creator.history']/text()"/>
                </p>
            </bibliography>
            <xsl:variable name="firstId">
                <xsl:value-of select="child::*[1][name()='record']/*[name()='object_number']" />
            </xsl:variable>
            <dsc type="othertype">
                <xsl:for-each select="*[name()='record']">
                    <xsl:if test="not(preceding-sibling::*[name()='record']/*[name()='object_number']/text() = ./*[name()='part_of_reference']/text())">
                        <xsl:call-template name="record_c">
                            <xsl:with-param name="firstId" select="$firstId"/>
                        </xsl:call-template>
                    </xsl:if>
                </xsl:for-each>
            </dsc>
        </archdesc>
    </xsl:template>

    <xsl:template name="record_c">
        <xsl:param name="firstId"/>
        <xsl:choose>
            <xsl:when test="not(./*[name()='object_number']/text() = $firstId)">
                <c>
                    <did>
                        <xsl:apply-templates select="*[not(name()='content.description' or name()='rights.notes' or name()='access_category.notes')]" mode="copy"/>
                    </did>
                    <xsl:apply-templates select="*[name()='content.description' or name()='rights.notes' or name()='access_category.notes']" mode="copy"/>
                    <xsl:if test="child::*[1][name()='record']/*[name()='content.subject'] or child::*[1][name()='record']/*[name()='content.person.name']">
                        <controlaccess>
                            <xsl:for-each select="child::*[1][name()='record']/*[name()='content.subject']">
                                <xsl:variable name="pos" select="position()"/>
                                <xsl:choose>
                                    <xsl:when test="../*[name()='content.subject.type'][position()=$pos] = 'place'">
                                        <geogname>
                                            <xsl:value-of select="./text()"/>
                                        </geogname>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:call-template name="excludeElement" />
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:for-each>
                            <xsl:for-each select="child::*[1][name()='record']/*[name()='content.person.name']">
                                <xsl:variable name="pos" select="position()"/>
                                <xsl:choose>
                                    <xsl:when test="../*[name()='content.person.name.type'][position()=$pos] = 'person'">
                                        <persname>
                                            <xsl:value-of select="./text()"/>
                                        </persname>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:call-template name="excludeElement" />
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:for-each>
                        </controlaccess>
                    </xsl:if>
                    <xsl:variable name="actual_id">
                        <xsl:value-of select="./*[name()='object_number']/text()"/>
                    </xsl:variable>
                    <xsl:for-each select="following-sibling::*[name()='record']">
                        <xsl:if test="./*[name()='part_of_reference']/text() = $actual_id">
                            <xsl:call-template name="record_c">
                                <xsl:with-param name="firstId" select="''"/>
                            </xsl:call-template>
                        </xsl:if>
                    </xsl:for-each>
                </c>
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="actual_id">
                    <xsl:value-of select="./*[name()='object_number']/text()"/>
                </xsl:variable>
                <xsl:for-each select="following-sibling::*[name()='record']">
                    <xsl:if test="./*[name()='part_of_reference']/text() = $actual_id">
                        <xsl:call-template name="record_c">
                            <xsl:with-param name="firstId" select="''"/>
                        </xsl:call-template>
                    </xsl:if>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
        <!--xsl:apply-templates select="following-sibling::*[name()='record']"-->
    </xsl:template>


    <xsl:template match="*[name()='edit.source']" mode="copy" />
    <xsl:template match="*[name()='input.source']" mode="copy" />
    <xsl:template match="*[name()='input.time']" mode="copy" />
    <xsl:template match="*[name()='input.name']" mode="copy" />
    <xsl:template match="*[name()='input.date']" mode="copy" />
    <xsl:template match="*[name()='edit.name']" mode="copy" />
    <xsl:template match="*[name()='edit.time']" mode="copy" />
    <xsl:template match="*[name()='edit.date']" mode="copy" />
    <xsl:template match="*[name()='creator.date_of_death']" mode="copy" />
    <xsl:template match="*[name()='creator.date_of_birth']" mode="copy" />

    
    <xsl:template match="*[name()='object_number']" mode="copy">
        <unitid encodinganalog="3.1.1" type="call number">
            <xsl:value-of select="." />
        </unitid>
    </xsl:template>

    <xsl:template match="*[lower-case(name())='title']" mode="copy">
        <unittitle encodinganalog="3.1.2">
            <xsl:value-of select="." />
        </unittitle>
    </xsl:template>

    <xsl:template match="*[name()='production.date.notes']" mode="copy">
        <unitdate calendar="gregorian" encodinganalog="3.1.3" era="ce">
            <xsl:choose>
                <xsl:when test="../*[name()='production.date.start'] and ../*[name()='production.date.end']">
                    <xsl:attribute name="normal">
                        <xsl:call-template name="unitdate">
                            <xsl:with-param name="date">
                                <xsl:value-of select="concat(../*[name()='production.date.start'], concat('/', ../*[name()='production.date.end']))"/>
                            </xsl:with-param>
                        </xsl:call-template>
                    </xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:variable name="normal">
                        <xsl:value-of select="ape:normalizeDate(normalize-space(.))"/>
                    </xsl:variable>
                    <xsl:if test="normalize-space($normal)">
                        <xsl:attribute name="normal">
                            <xsl:value-of select="$normal"/>
                        </xsl:attribute>
                    </xsl:if>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="." />
        </unitdate>
    </xsl:template>

    <xsl:template match="*[name()='production.date.start' or name()='production.date.end']" mode="copy" />

    <xsl:template match="*[name()='creator']" mode="copy">
        <origination encodinganalog="3.2.1">
            <xsl:value-of select="."/>
        </origination>
    </xsl:template>

    <xsl:template match="*[name()='content.description']" mode="copy">
        <scopecontent encodinganalog="summary">
            <p>
                <xsl:value-of select="."/>
            </p>
        </scopecontent>
    </xsl:template>

    <xsl:template match="*[name()='rights.notes']" mode="copy">
        <userestrict>
            <p>
                <xsl:value-of select="."/>
            </p>
        </userestrict>
    </xsl:template>

    <xsl:template match="*[name()='access_category.notes']" mode="copy">
        <accessrestrict>
            <p>
                <xsl:value-of select="."/>
            </p>
        </accessrestrict>
    </xsl:template>

    <xsl:template match="*[name()='inscription.language']" mode="copy">
        <langmaterial>
            <language> <!--TODO with languages.xml file-->
                <xsl:value-of select="."/>
            </language>
        </langmaterial>
    </xsl:template>

    <xsl:template match="*[name()='dimension.free']" mode="copy">
        <physdesc encodinganalog="3.1.5">
            <dimensions><xsl:value-of select="."/></dimensions>
        </physdesc>
    </xsl:template>

    <xsl:template name="unitdate">
        <xsl:param name="date"/>
        <xsl:value-of select="ape:normalizeDate($date)"/>
    </xsl:template>

    <!--Not used for sure or already used above-->
    <xsl:template match="*[name()='record_type']" mode="copy" />
    <xsl:template match="*[name()='content.subject']" mode="copy" />
    <xsl:template match="*[name()='content.subject.type']" mode="copy" />
    <xsl:template match="*[name()='content.person.name']" mode="copy" />
    <xsl:template match="*[name()='content.person.name.type']" mode="copy" />
    <xsl:template match="*[name()='priref']" mode="copy" />
    <xsl:template match="*[name()='description_level']" mode="copy" />
    <xsl:template match="*[name()='parts.description_level']" mode="copy" />
    <xsl:template match="*[name()='part_of_reference']" mode="copy" />
    <xsl:template match="*[name()='parts_reference']" mode="copy" />
    <xsl:template match="*[name()='diagnostic']" mode="copy" />
    <xsl:template match="*[name()='record_access.owner']" mode="copy" />
    <xsl:template match="*[name()='parts.title']" mode="copy" />
    <xsl:template match="*[name()='part_of.description_level']" mode="copy" />
    <xsl:template match="*[name()='part_of.title']" mode="copy" />
    <xsl:template match="*[name()='location.default']" mode="copy" />
    <xsl:template match="*[name()='creator.history']" mode="copy" />
    <xsl:template match="*[name()='rules_conventions']" mode="copy" />
    <!--Replaced by mainagencycode - so we do not use-->
    <xsl:template match="*[name()='institution.code']" mode="copy" />
    <!--We do not know what those are in EAD-->
    <xsl:template match="*[name()='reproduction.creator']" mode="copy" />
    <xsl:template match="*[name()='reproduction.type']" mode="copy" />
    <xsl:template match="*[name()='reproduction.date']" mode="copy" />
    <xsl:template match="*[name()='reproduction.reference']" mode="copy" />
    <xsl:template match="*[name()='reproduction.format']" mode="copy" />

</xsl:stylesheet>