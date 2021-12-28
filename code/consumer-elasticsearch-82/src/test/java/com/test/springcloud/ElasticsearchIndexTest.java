package com.test.springcloud;

import com.test.springcloud.dto.ProductQueryDTO;
import com.test.springcloud.po.Product;
import com.test.springcloud.repository.ProductRepository;
import com.test.springcloud.util.ProductQueryHelper;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchIndexTest {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private ProductRepository productRepository;

    /**
     * 创建索引库，默认自动创建
     */
    @Test
    public void createIndex(){
        System.out.println("创建索引");
    }

    /**
     * 删除索引库
     */
    @Test
    public void deleteIndex(){
        boolean flag = elasticsearchRestTemplate.deleteIndex(Product.class);
        System.out.println("删除所有=" + flag);
    }

    //-------------------------------document操作------------------------------------------
    /**
     * 新增document
     */
    @Test
    public void save1(){
        Product product = new Product();
        product.setId(1L);
        product.setTitle("小米手机");
        product.setCategory("手机");
        product.setPrice(1999.0);
        product.setImages("http://www.xxx/xm.jpg");
        productRepository.save(product);
        System.out.println("小米，保存成功！");
    }

    /**
     * 新增document
     */
    @Test
    public void save2(){
        Product product = new Product();
        product.setId(2L);
        product.setTitle("华为手机");
        product.setCategory("手机");
        product.setPrice(2999.0);
        product.setImages("http://www.xxx/hw.jpg");
        productRepository.save(product);
        System.out.println("华为，保存成功！");
    }

    /**
     * 批量保存
     */
    @Test
    public void saveAll() {
        List<Product> productList = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            Product product = new Product();
            product.setId((long) i);
            product.setTitle("小米手机" + i);
            product.setCategory("手机");
            product.setPrice(2999.0 + i);
            product.setImages("http://www.xiaomi.com/xm.jpg");
            productList.add(product);
        }
        productRepository.saveAll(productList);
        System.out.println("小米，批量保存成功！");
    }

    /**
     * 修改
     */
    @Test
    public void update(){//修改小米手机信息
        Product product = new Product();
        product.setId(1L);//id相同则修改
        product.setTitle("小米手机");
        product.setCategory("手机");
        product.setPrice(1999.0);
        product.setImages("http://www.xxx/xm.jpg");
        productRepository.save(product);
        System.out.println("小米，修改保存成功！");
    }


    /**
     * 通过id查询：product
     */
    @Test
    public void findById(){
        Product product = productRepository.findById(1L).get();
        System.out.println(product);
    }

    /**
     * 查询所有
     */
    @Test
    public void findAll() {
        Iterable<Product> all = productRepository.findAll();
        all.forEach(xx -> {
            System.out.println(xx);
        });
    }

    /**
     * 删除document
     */
    @Test
    public void delete() {
        Product product = new Product();
        product.setId(1L);
        productRepository.delete(product);
        System.out.println("删除成功！");
    }

    /**
     * 分页查询
     */
    @Test
    public void findByPageable() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");//排序id
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page,size,sort);
        Page<Product> productPage = productRepository.findAll(pageRequest);
        productPage.forEach(xx ->{
            System.out.println(xx);
        });
    }

    /**
     * 条件查询：模糊查询，精确查询，组合查询
     */
    @Test
    public void search() {

        ProductQueryDTO query = new ProductQueryDTO();
        query.setCategory("手机");
        query.setTitle("手机8");//模糊匹配 查询

        //查询条件封装
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(ProductQueryHelper.wrapperQuery(query))
                .withPageable(PageRequest.of(query.getPage() - 1, query.getSize()));

        //排序
        queryBuilder.withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC));

        Page<Product> page = productRepository.search(queryBuilder.build());
        page.forEach(xx -> {
            System.out.println(xx);
        });


    }

}
