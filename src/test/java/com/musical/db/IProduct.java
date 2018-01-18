package com.musical.db;

import com.musical.models.Product;
import org.apache.ibatis.annotations.Insert;

public interface IProduct {

    @Insert("insert into t_analyse_result values (#{product.body_size},#{product.collect_time},#{product.connect_duration},#{product.create_time},#{product.dns_duration},#{product.domain_name},#{product.file_id},#{product.id},#{product.info},#{product.ip_address},#{product.modify_time},#{product.product},#{product.receive_duration},#{product.region},#{product.test_type},#{product.total_duration},#{product.url_count},#{product.wait_duration})")
    int add(Product product);

}
