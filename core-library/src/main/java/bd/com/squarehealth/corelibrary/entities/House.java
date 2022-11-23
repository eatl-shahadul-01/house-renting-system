package bd.com.squarehealth.corelibrary.entities;

import bd.com.squarehealth.corelibrary.enumerations.PostStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({ "bookings" })
@Entity(name = "House")
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "price")
    @Range(min = 3500000L, max = 25000000L)
    private double price;

    @Column(name = "description")
    @Size(min = 1, max = 512)
    private String description;

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "addressId")
    private Address address;

    //<!-- VARIABLES FOR POST CREATOR --!>//
    @Column(name = "postStatus")
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus = PostStatus.NONE;

    @Column(name = "postedBy")
    private Long postedBy;

    //<!-- VARIABLES FOR BOOKING --!>//
    @OneToMany(mappedBy = "house")
    private Set<Booking> bookings;
}
