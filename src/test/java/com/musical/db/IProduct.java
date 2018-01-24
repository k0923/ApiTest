package com.musical.db;

import com.musical.models.Product;
import org.apache.ibatis.annotations.Insert;

public interface IProduct {

    @Insert("insert into t_analyse_result (body_size,collect_time,connect_duration,create_time,dns_duration,domain_name,file_id,id,info,ip_address,modify_time,product,receive_duration,region,test_type,total_duration,url_count,wait_duration) values (#{body_size},#{collect_time},#{connect_duration},#{create_time},#{dns_duration},#{domain_name},#{file_id},#{id},#{info},#{ip_address},#{modify_time},#{product},#{receive_duration},#{region},#{test_type},#{total_duration},#{url_count},#{wait_duration})")
    int add(Product product);

}
