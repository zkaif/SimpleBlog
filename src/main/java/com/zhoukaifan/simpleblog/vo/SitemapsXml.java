package com.zhoukaifan.simpleblog.vo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA. User: ZhouKaifan Date:2018/9/6 Time:下午7:39
 */
@XmlRootElement(name = "urlset")
@XmlAccessorType(XmlAccessType.NONE)
public class SitemapsXml {

    @XmlElement(name="url")
    private List<SitemapsUrl> url = new ArrayList<>();
    @XmlAttribute(name="xmlns")
    private String xmlns="http://www.sitemaps.org/schemas/sitemap/0.9";
    @XmlAttribute(name="xmlns:mobile")
    private String xmlnsmobile="http://www.google.com/schemas/sitemap-mobile/1.0";
    @XmlAccessorType(XmlAccessType.NONE)
    public static class SitemapsUrl {

        @XmlElement(name="loc")
        private String loc;
        @XmlElement(name="mobile:mobile")
        private Mobile mobile;
        @XmlElement(name="lastmod")
        private String lastmod;
        @XmlElement(name="changefreq")
        private String changefreq = "weekly";
        @XmlElement(name="priority")
        private String priority = "0.8";

        @XmlAccessorType(XmlAccessType.NONE)
        public static class Mobile{
            @XmlAttribute(name="type")
            private String type = "pc,mobile";

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Mobile() {
            }

            public Mobile(String type) {
                this.type = type;
            }
        }

        public String getLoc() {
            return loc;
        }

        public void setLoc(String loc) {
            this.loc = loc;
        }

        public Mobile getMobile() {
            return mobile;
        }

        public void setMobile(Mobile mobile) {
            this.mobile = mobile;
        }

        public String getLastmod() {
            return lastmod;
        }

        public void setLastmod(String lastmod) {
            this.lastmod = lastmod;
        }

        public String getChangefreq() {
            return changefreq;
        }

        public void setChangefreq(String changefreq) {
            this.changefreq = changefreq;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public SitemapsUrl() {
        }

        public SitemapsUrl(String loc, String mobile, String lastmod, String changefreq,
                String priority) {
            this.loc = loc;
            this.mobile = new Mobile(mobile);
            this.lastmod = lastmod;
            this.changefreq = changefreq;
            this.priority = priority;
        }
    }

    public List<SitemapsUrl> getUrl() {
        return url;
    }

    public void setUrl(List<SitemapsUrl> url) {
        this.url = url;
    }

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }
}
