package dev.Innocent.EventManagementSystem.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ConstantsConfig {
    @Bean
    public List<String> eventCategory(){
        return Arrays.asList(
                "Food & Drink",
                "Film, Media & Entertainment",
                "Special Interest",
                "Religious & Spirituality",
                "Technology",
                "Government & Politics",
                "Arts & Culture",
                "Sports & Recreation",
                "Education & Learning",
                "Health & Wellness",
                "Business & Entrepreneurship",
                "Travel & Adventure",
                "Community & Social",
                "Fashion & Beauty",
                "Science & Innovation",
                "Environmental & Sustainability",
                "Food & Wine",
                "Music & Concerts",
                "Literature & Books",
                "Family & Parenting",
                "Charity & Fundraising",
                "Hobbies & Interests",
                "History & Heritage",
                "Motivation & Self-Improvement",
                "Science Fiction & Fantasy",
                "Pets & Animals"
        );
    }
    @Bean
    public List<String> ticketTypes (){
        return Arrays.asList(
                "VVIP Ticket",
                "VIP Ticket",
                "Regular Ticket"
        );
    }
}
