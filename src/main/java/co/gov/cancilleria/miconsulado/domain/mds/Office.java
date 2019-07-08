package co.gov.cancilleria.miconsulado.domain.mds;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * The Council office  entity.
 */
@ApiModel(description = "The Council office entity.")
@Entity
@Table(name = "vOficinas", schema = "mdm")
public class Office implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @ApiModelProperty(value = "The name attribute.")
    @Column(name = "[Nombre Largo SITAC]")
    private String name;

    @ApiModelProperty(value = "The country attribute.")
    @Column(name = "Pais")
    private String country;

    @ApiModelProperty(value = "The address attribute.")
    @Column(name = "Dirección")
    private String address;

    @ApiModelProperty(value = "The opening hours attribute.")
    @Column(name = "[Horario atención]")
    private String openingHours;

    @ApiModelProperty(value = "The email attribute.")
    @Column(name = "Email")
    private String email;

    @ApiModelProperty(value = "The phone 1 attribute.")
    @Column(name = "Teléfono")
    private String phone1;

    @ApiModelProperty(value = "The phone 2 attribute.")
    @Column(name = "[Teléfono 2]")
    private String phone2;

    @ApiModelProperty(value = "The emergency phone attribute.")
    @Column(name = "[Télefono emergencia]")
    private String emergencyPhone;

    @ApiModelProperty(value = "The latitude attribute.")
    @Column(name = "Latitud")
    private Double latitude;

    @ApiModelProperty(value = "The longitude attribute.")
    @Column(name = "Longitud")
    private Double longitude;

    @ApiModelProperty(value = "The site URL attribute.")
    @Column(name = "[URL Oficina]")
    private String siteUrl;

    @ApiModelProperty(value = "The facebook WEB URL attribute.")
    @Column(name = "[url facebook]")
    private String facebookWebUrl;

    @ApiModelProperty(value = "The twitter WEB URL attribute.")
    @Column(name = "[url twitter]")
    private String twitterWebUrl;

    @ApiModelProperty(value = "The instagram WEB URL attribute.")
    @Column(name = "[url instagram]")
    private String instagramWebUrl;

    @ApiModelProperty(value = "The facebook APP URL attribute.")
    @Column(name = "[url facebook app]")
    private String facebookAppUrl;

    @ApiModelProperty(value = "The twitter APP URL attribute.")
    @Column(name = "[url twitter app]")
    private String twitterAppUrl;

    @ApiModelProperty(value = "The instagram APP URL attribute.")
    @Column(name = "[url instagram app]")
    private String instagramAppUrl;

    @ApiModelProperty(value = "The consul name attribute.")
    @Column(name = "[VersionFlag]")
    private String consulName;

    @JsonIgnore
    @Column(name = "[Atención Ciudadano_Code]")
    private Integer citizenAttentionCode;

    @JsonIgnore
    @Column(name = "Estado")
    private Integer status;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    /**
     * The id attribute.
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * The name attribute.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getFacebookWebUrl() {
        return facebookWebUrl;
    }

    public void setFacebookWebUrl(String facebookWebUrl) {
        this.facebookWebUrl = facebookWebUrl;
    }

    public String getTwitterWebUrl() {
        return twitterWebUrl;
    }

    public void setTwitterWebUrl(String twitterWebUrl) {
        this.twitterWebUrl = twitterWebUrl;
    }

    public String getInstagramWebUrl() {
        return instagramWebUrl;
    }

    public void setInstagramWebUrl(String instagramWebUrl) {
        this.instagramWebUrl = instagramWebUrl;
    }

    public String getFacebookAppUrl() {
        return facebookAppUrl;
    }

    public void setFacebookAppUrl(String facebookAppUrl) {
        this.facebookAppUrl = facebookAppUrl;
    }

    public String getTwitterAppUrl() {
        return twitterAppUrl;
    }

    public void setTwitterAppUrl(String twitterAppUrl) {
        this.twitterAppUrl = twitterAppUrl;
    }

    public String getInstagramAppUrl() {
        return instagramAppUrl;
    }

    public void setInstagramAppUrl(String instagramAppUrl) {
        this.instagramAppUrl = instagramAppUrl;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Office office = (Office) o;
        return id.equals(office.id) &&
            name.equals(office.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Office{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }

    public String getConsulName() {
        return consulName;
    }

    public void setConsulName(String consulName) {
        this.consulName = consulName;
    }
}
