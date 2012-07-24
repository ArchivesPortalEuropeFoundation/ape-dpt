<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns="urn:isbn:1-931666-22-9"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:ape="http://www.archivesportaleurope.eu/functions"
                xsi:schemaLocation="urn:isbn:1-931666-22-9 http://www.loc.gov/ead/ead.xsd"
                xpath-default-namespace="urn:isbn:1-931666-22-9"
                exclude-result-prefixes="xsl ape">

    <!-- c-level -->
    <xsl:template match="c | c01 | c02 | c03 | c04 | c05 | c06 | c07 | c08 | c09 | c10 | c11 | c12" mode="copy fonds intermediate lowest">
        <!--Counter-->
        <xsl:value-of select="ape:counterclevel()"/>
        <!---->
        <xsl:variable name="level">
            <xsl:choose>
                <xsl:when test="@level='item'">
                    <xsl:choose>
                        <xsl:when test="(child::c[@level='item'])"><!-- or (following-sibling::*/child::*/@level='item') or (preceding-sibling::*/child::*/@level='item')">-->
                            <xsl:value-of select="'file'"/>
                        </xsl:when>
                        <xsl:when test="child::c[not(@level)] or child::c[@otherlevel='partie-de-pièce']">
                            <xsl:value-of select="'file'"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="'item'"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when test="@level='file'">
                    <xsl:choose>
                        <xsl:when test="child::c[@level='file'] or child::c[@otherlevel='sous-dossier'] or (child::c[@otherlevel='subfile' or @otherlevel='item'] and child::c/child::c[@level='item'])"><!-- or (following-sibling::c/child::c[@otherlevel='subfile' or @otherlevel='item'] and following-sibling::c/child::c/child::c[@level='item']) or (preceding-sibling::c/child::c[@otherlevel='subfile' or @otherlevel='item'] and preceding-sibling::c/child::c/child::c[@level='item'])">-->
                            <xsl:value-of select="'subseries'"/>
                        </xsl:when>
                        <xsl:when test="parent::c[@otherlevel='subfile'] and parent::c/parent::c[@otherlevel='filegrp']">
                            <xsl:value-of select="'item'"/>
                        </xsl:when>
                        <xsl:when test="parent::c[not(@level)]">
                            <xsl:value-of select="'subseries'"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="'file'"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when test="@level='subseries' or @level='recordgrp' or @level='subgrp' or (@level='series' and //eadid/@countrycode = 'DE')">
                    <xsl:choose>
                        <xsl:when test="child::c[@level='series']">
                            <xsl:value-of select="'series'"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="'subseries'"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when test="(@level='series' and (//eadid/@countrycode != 'DE' or not(//eadid/@countrycode))) or @level='subfonds' or @level='class' or not(@level)">
                    <xsl:choose>
                        <xsl:when test="parent::c[@otherlevel='filegrp'] or (not(@level) and parent::c[@level='subseries'])">
                            <xsl:value-of select="'file'"/>
                        </xsl:when>
                        <xsl:when test="child::c[@level='item']"><!-- or following-sibling::c/child::c[@level='item'] or preceding-sibling::c/child::c[@level='item']">-->
                            <xsl:value-of select="'file'"/>
                        </xsl:when>
                        <xsl:when test="@level='subfonds' and parent::c[@level='subseries']">
                            <xsl:value-of select="'subseries'" />
                        </xsl:when>
                        <xsl:when test="not(@level) and parent::c[@level='file' or @level='item']">
                            <xsl:value-of select="'item'"/>
                        </xsl:when>
                        <xsl:when test="parent::c[@level='file'] and not(child::c)">
                            <xsl:value-of select="'item'"/>
                        </xsl:when>
                        <xsl:when test="parent::c[@level='file'] and child::c">
                            <xsl:value-of select="'file'"/>
                        </xsl:when>
                        <xsl:when test="parent::c[@otherlevel='groupe-de-fonds' or @otherlevel='groupe_de_fonds' or not(@level)]">
                            <xsl:value-of select="'subseries'"/>
                        </xsl:when>
                        <xsl:when test="not(@level) and parent::c[@otherlevel='sous-sous-serie-organique' or @otherlevel='recordgrp']">
                            <xsl:value-of select="'subseries'"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="'series'"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when test="@level='fonds' or @level='collection'">
                    <xsl:choose>
                        <xsl:when test="parent::c[@otherlevel='groupe-de-fonds' or @otherlevel='groupe_de_fonds' or @otherlevel='groupe-de-fonds-et-de-collections' or not(@level)]">
                            <xsl:value-of select="'series'"/>
                        </xsl:when>
                        <xsl:when test="parent::c[@level='fonds']/parent::c[not(@level) or @otherlevel='groupe-de-fonds']">
                            <xsl:value-of select="'series'"/>
                        </xsl:when>
                        <xsl:when test="parent::c[@level='subseries']">
                            <xsl:value-of select="'subseries'"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="'fonds'"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when test="@level='otherlevel'">
                    <xsl:choose>
                        <xsl:when test="@otherlevel='filegrp' or @otherlevel=' filegrp' or @otherlevel='filegroup' or @otherlevel='file' or @otherlevel='subseries' or @otherlevel='subsubseries' or @otherlevel='sous-serie' or @otherlevel='sous-sous-serie' or @otherlevel='sous-sous-sous-serie' or @otherlevel='subsubsubseries' or @otherlevel='subsubsusbseries' or @otherlevel='subsubsubsubseries' or @otherlevel='sous-sous-série' or @otherlevel='sous-sous-sous-série' or @otherlevel='partie-de-subgp' or @otherlevel='partie-de-subgrp' or @otherlevel='partie-de-sbgrp' or @otherlevel='subsubsubsubsubseries'">
                            <xsl:value-of select="'subseries'"/>
                        </xsl:when>
                        <xsl:when test="((@otherlevel='groupe-de-fonds' or @otherlevel='groupe_de_fonds') and not(parent::c[not(@level) or @level='file' or @otherlevel='groupe-de-fonds' or @otherlevel='groupe_de_fonds'])) or @otherlevel='SF' or @otherlevel='sous-fonds' or @otherlevel='sous-sous-fonds'">
                            <xsl:value-of select="'fonds'"/>
                        </xsl:when>
                        <xsl:when test="@otherlevel='groupe-de-fonds-et-de-collections' or @otherlevel='groupe-de-series' or @otherlevel='groupe-de-fonds-et-de-sous-fonds' or @otherlevel='SC' or @otherlevel='SSC' or @otherlevel='SSSC' or @otherlevel='SR' or @otherlevel='SSR' or @otherlevel='SSSR' or @otherlevel='partie-de-fonds' or @otherlevel='partie-de-sous-fonds' or @otherlevel='sous-collection'">
                            <xsl:value-of select="'series'"/>
                        </xsl:when>
                        <xsl:when test="@otherlevel='sous-sous-serie-organique' or @otherlevel='DC' or ((@otherlevel='groupe-de-fonds' or @otherlevel='groupe_de_fonds') and parent::c[not(@level) or @otherlevel='groupe-de-fonds' or @otherlevel='groupe_de_fonds'])">
                            <xsl:value-of select="'series'"/>
                        </xsl:when>
                        <xsl:when test="@otherlevel='UI' or @otherlevel='volym' or @otherlevel='karta' or (@otherlevel='sous-dossier' and child::c[@otherlevel='sous-sous-dossier'])">
                            <xsl:value-of select="'file'"/>
                        </xsl:when>
                        <xsl:when test="@otherlevel='D' or ((@otherlevel='groupe-de-fonds' or @otherlevel='groupe_de_fonds') and parent::c[@level='file']) or @otherlevel='partie-de-pièce' or @otherlevel='partie-de-dossier' or @otherlevel='sous-dossier' or @otherlevel='sous-sous-dossier'">
                            <xsl:value-of select="'item'"/>
                        </xsl:when>
                        <xsl:when test="@otherlevel='subfile'">
                            <xsl:choose>
                                <xsl:when test="child::c[@level='file' or @level='item' or @otherlevel='subfile']"><!-- or following-sibling::c/child::c[@level='file' or @level='item' or @otherlevel='subfile'] or preceding-sibling::c/child::c[@level='file' or @level='item' or @otherlevel='subfile']">-->
                                    <xsl:value-of select="'file'"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="'item'"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:when test="@otherlevel='arkusz'">
                            <xsl:value-of select="'item'"/>
                        </xsl:when>
                        <xsl:when test="not(@otherlevel) and child::c[@level='series']">
                            <xsl:value-of select="'series'"/>
                        </xsl:when>
                        <xsl:when test="not(@otherlevel)">
                            <xsl:value-of select="'subseries'"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:message select="@otherlevel"> - WARNING! We don't know this c otherlevel attribute</xsl:message>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:message select="@level"> - WARNING! We don't know this c level attribute</xsl:message>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="@level='collection' and did/unitid='' and did/unittitle=''">
                <xsl:apply-templates select="child::*[not(name()='did')]" mode="copy"/>
            </xsl:when>
            <xsl:otherwise>
                <c level="{$level}">
                    <!--<xsl:param name="levelprecedent" select="$level" />-->
                    <!--xsl:if test="normalize-space(@id)">
                        <xsl:attribute name="id" select="normalize-space(@id)"/>
                    </xsl:if-->
                    <xsl:attribute name="id">
                        <xsl:choose>
                            <xsl:when test="normalize-space(@id)">
                                <xsl:value-of select="normalize-space(@id)"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="generate-id()"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>
                    <xsl:if test="$level='item'">
                        <xsl:attribute name="encodinganalog" select="'3.1.4'"/>
                        <xsl:apply-templates mode="lowest"/>
                    </xsl:if>
                    <xsl:if test="$level='file'">
                        <xsl:attribute name="encodinganalog" select="'3.1.4'"/>
                        <xsl:apply-templates mode="lowest"/>
                    </xsl:if>
                    <xsl:if test="$level='subseries'">
                        <xsl:attribute name="encodinganalog" select="'3.1.4'"/>
                        <xsl:apply-templates mode="intermediate"/>
                    </xsl:if>
                    <xsl:if test="$level='series'">
                        <xsl:attribute name="encodinganalog" select="'3.1.4'"/>
                        <xsl:apply-templates mode="intermediate"/>
                    </xsl:if>
                    <xsl:if test="$level='fonds'">
                        <xsl:attribute name="encodinganalog" select="'3.1.4'"/>
                        <xsl:apply-templates mode="fonds"/>
                    </xsl:if>
                </c>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>