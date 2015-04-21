package crypto.util.signature.model;

/**
 *
 * @author Miguel Pazo (http://miguelpazo.com/)
 *
 * Standar RFC 5280 with current bouncycastle version:
 *
 * C = Country name ST = State or province L = Locality O = Organization name OU
 * = Organization unit CN = Common name
 *
 */
public class Subject {

    private String country;
    private String province;
    private String locality;
    private String orgName;
    private String orgUnitName;
    private String commonName;

    /**
     *
     * @return Country Name (2 letter code)
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country Country Name (2 letter code)
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return State or Province Name (full name)
     */
    public String getProvince() {
        return province;
    }

    /**
     *
     * @param province State or Province Name (full name)
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     *
     * @return Locality Name (city, district)
     */
    public String getLocality() {
        return locality;
    }

    /**
     *
     * @param locality Locality Name (city, district)
     */
    public void setLocality(String locality) {
        this.locality = locality;
    }

    /**
     *
     * @return Organization Name (company)
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     *
     * @param orgName Organization Name (company)
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     *
     * @return Organizational Unit Name (department, division)
     */
    public String getOrgUnitName() {
        return orgUnitName;
    }

    /**
     *
     * @param orgUnitName Organizational Unit Name (department, division)
     */
    public void setOrgUnitName(String orgUnitName) {
        this.orgUnitName = orgUnitName;
    }

    /**
     *
     * @return Common Name (hostname, IP, or your name)
     */
    public String getCommonName() {
        return commonName;
    }

    /**
     *
     * @param commonName Common Name (hostname, IP, or your name)
     */
    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    @Override
    public String toString() {
        String subject = "";

        if (country != null && !country.equals("")) {
            subject += "C=" + country;
        }

        if (province != null && !province.equals("")) {
            subject += ";ST=" + province;
        }

        if (locality != null && !locality.equals("")) {
            subject += ";L=" + locality;
        }

        if (orgName != null && !orgName.equals("")) {
            subject += ";O=" + orgName;
        }

        if (orgUnitName != null && !orgUnitName.equals("")) {
            subject += ";OU=" + orgUnitName;
        }

        if (commonName != null && !commonName.equals("")) {
            subject += ";CN=" + commonName;
        }

        if (subject.substring(0, 1).equals(";")) {
            subject = subject.substring(1);
        }

        return subject;
    }

}
