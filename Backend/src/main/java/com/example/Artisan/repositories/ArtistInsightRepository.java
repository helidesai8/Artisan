package com.example.Artisan.repositories;

import com.example.Artisan.DTOs.ArtistCategoryInsight;
import com.example.Artisan.DTOs.ArtistYearlyInsight;
import com.example.Artisan.DTOs.CategoryRatingInsight;
import com.example.Artisan.DTOs.products.ProductSales;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface ArtistInsightRepository {
    @Query("""
            Select pc.name as name, Sum(oi.amount) as amount
            from OrderItem oi inner join oi.product p
            inner join p.category pc
            inner join p.artist a
            where a.artistId = ?1
            group by pc.name""")
    ArrayList<ArtistCategoryInsight> getArtistSalesByCategory(float artistId);

    @Query("""
            Select Year(od.orderDate) as year, Month(od.orderDate) as month, Sum(oi.amount) as amount
            from OrderDetail od inner join od.orderItems oi
            inner join oi.product p
            inner join p.category c
            inner join p.artist a
            where a.artistId = ?1 and Year(od.orderDate) >= Year(Now()) - ?2
            group by Year(od.orderDate), Month(od.orderDate)
            order by Year(od.orderDate) desc""")
    ArrayList<ArtistYearlyInsight> getArtistYearlySales(float artistId, int numberOfYears);

    @Query("""
        Select Round(Avg(upr.rating),1) as rating, pc.name as name
        from Artist a inner join a.products p
        inner join p.category pc
        inner join p.userProductRatings upr
        where a.artistId = ?1
        group by pc.name""")
    ArrayList<CategoryRatingInsight> getArtistCategoryRating(float artistId);

    @Query("""
            Select Date(od.orderDate) as orderDate, p.name as name, p.price as price, oi.quantity as quantity,
            u.email as orderBy
            from OrderDetail od inner join od.orderItems oi
            inner join oi.product p
            inner join p.artist a
            inner join od.user u
            where a.artistId = ?1
            order by od.orderDate desc
            LIMIT ?2""")
    ArrayList<ProductSales> getLatestSales(float artistId, int saleRecordCount);
}
