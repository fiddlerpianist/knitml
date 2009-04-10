<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"    
                xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
    <xsl:import href="/Progra~1/eclipse/plugins/com.oxygenxml.editor_9.0.0/frameworks/docbook/xsl/fo/docbook.xsl"/>
    <xsl:attribute-set name="formal.object.properties">
        <xsl:attribute name="keep-together.within-column">auto</xsl:attribute>
    </xsl:attribute-set>
        
    <xsl:template name="user.pagemasters">
        <fo:simple-page-master master-name="landscape-first"
            page-width="{$page.width}"
            page-height="{$page.height}"
            margin-top="{$page.margin.top}"
            margin-bottom="{$page.margin.bottom}"
            margin-left="{$margin.left.inner}"
            margin-right="{$page.margin.outer}">
            <xsl:if test="$axf.extensions != 0">
                <xsl:call-template name="axf-page-master-properties">
                    <xsl:with-param name="page.master">landscape-first</xsl:with-param>
                </xsl:call-template>
            </xsl:if>
            <fo:region-body margin-bottom="{$body.margin.bottom}"
                margin-top="{$body.margin.top}"
                column-gap="{$column.gap.body}"
                column-count="{$column.count.body}"
                reference-orientation="90">
            </fo:region-body>
            <fo:region-before region-name="xsl-region-before-first"
                extent="{$region.before.extent}"
                display-align="before"/>
            <fo:region-after region-name="xsl-region-after-first"
                extent="{$region.after.extent}"
                display-align="after"/>
        </fo:simple-page-master>
        
        <fo:simple-page-master master-name="landscape-odd"
            page-width="{$page.width}"
            page-height="{$page.height}"
            margin-top="{$page.margin.top}"
            margin-bottom="{$page.margin.bottom}"
            margin-left="{$margin.left.inner}"
            margin-right="{$page.margin.outer}">
            <xsl:if test="$axf.extensions != 0">
                <xsl:call-template name="axf-page-master-properties">
                    <xsl:with-param name="page.master">landscape-odd</xsl:with-param>
                </xsl:call-template>
            </xsl:if>
            <fo:region-body margin-bottom="{$body.margin.bottom}"
                margin-top="{$body.margin.top}"
                column-gap="{$column.gap.body}"
                column-count="{$column.count.body}"
                reference-orientation="90">
            </fo:region-body>
            <fo:region-before region-name="xsl-region-before-odd"
                extent="{$region.before.extent}"
                display-align="before"/>
            <fo:region-after region-name="xsl-region-after-odd"
                extent="{$region.after.extent}"
                display-align="after"/>
        </fo:simple-page-master>
        
        <fo:simple-page-master master-name="landscape-even"
            page-width="{$page.width}"
            page-height="{$page.height}"
            margin-top="{$page.margin.top}"
            margin-bottom="{$page.margin.bottom}"
            margin-left="{$margin.left.outer}"
            margin-right="{$page.margin.inner}">
            <xsl:if test="$axf.extensions != 0">
                <xsl:call-template name="axf-page-master-properties">
                    <xsl:with-param name="page.master">landscape-even</xsl:with-param>
                </xsl:call-template>
            </xsl:if>
            <fo:region-body margin-bottom="{$body.margin.bottom}"
                margin-top="{$body.margin.top}"
                column-gap="{$column.gap.body}"
                column-count="{$column.count.body}"
                reference-orientation="90">
            </fo:region-body>
            <fo:region-before region-name="xsl-region-before-even"
                extent="{$region.before.extent}"
                display-align="before"/>
            <fo:region-after region-name="xsl-region-after-even"
                extent="{$region.after.extent}"
                display-align="after"/>
        </fo:simple-page-master>
        
        <fo:page-sequence-master master-name="landscape">
            <fo:repeatable-page-master-alternatives>
                <fo:conditional-page-master-reference master-reference="blank"
                    blank-or-not-blank="blank"/>
                <fo:conditional-page-master-reference master-reference="landscape-first"
                    page-position="first"/>
                <fo:conditional-page-master-reference master-reference="landscape-odd"
                    odd-or-even="odd"/>
                <fo:conditional-page-master-reference 
                    odd-or-even="even">
                    <xsl:attribute name="master-reference">
                        <xsl:choose>
                            <xsl:when test="$double.sided != 0">landscape-even</xsl:when>
                            <xsl:otherwise>landscape-odd</xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>
                </fo:conditional-page-master-reference>
            </fo:repeatable-page-master-alternatives>
        </fo:page-sequence-master>
    </xsl:template>

    <xsl:template name="select.user.pagemaster">
        <xsl:param name="element"/>
        <xsl:param name="pageclass"/>
        <xsl:param name="default-pagemaster"/>
        
        <xsl:choose>
            <xsl:when test="@role = 'landscape'">landscape</xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$default-pagemaster"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>        
</xsl:stylesheet> 
