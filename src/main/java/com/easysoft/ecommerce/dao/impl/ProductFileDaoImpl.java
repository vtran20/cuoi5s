package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.ProductFileDao;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.ProductFile;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductFileDaoImpl extends GenericDaoImpl<ProductFile, Long> implements ProductFileDao {

    @Override
    public List<ProductFile> findProductFiles (Long productId, String fileType) {
        if (StringUtils.isEmpty(fileType)) {
            Query query = getSessionFactory().getCurrentSession().createQuery("select f from ProductFile f where f.product.id = :prodId")
                    .setParameter("prodId", productId);
            return query.list();
        } else {
            Query query = getSessionFactory().getCurrentSession().createQuery("select f from ProductFile f where f.product.id = :prodId and f.fileType = :fileType")
                    .setParameter("prodId", productId)
                    .setParameter("fileType", fileType);
            return query.list();
        }
    }

    @Override
    public ProductFile getDefaultImage (Long productId, String fileType) {
        Query query = getSessionFactory().getCurrentSession().createQuery("select f from ProductFile f where f.product.id = :prodId and f.fileType = :fileType and f.default = 'Y'")
                .setParameter("prodId", productId)
                .setParameter("fileType", fileType);
        List list =  query.list();
        if (list != null && list.size() > 0) {
            return (ProductFile) list.get(0);
        } else {
            query = getSessionFactory().getCurrentSession().createQuery("select f from ProductFile f where f.product.id = :prodId and f.fileType = :fileType")
                    .setParameter("prodId", productId)
                    .setParameter("fileType", fileType);
            list = query.list();
            if (list != null && list.size() > 0) {
                return (ProductFile) list.get(0);
            }
        }

        return null;
    }

    @Override
    public void deleteProductFiles(Long productId, String fileType) {
        getSessionFactory().getCurrentSession().createQuery("delete from ProductFile f where f.product.id = :prodId and f.fileType = :fileType")
                .setParameter("prodId", productId)
                .setParameter("fileType", fileType).executeUpdate();
    }

    @Override
    public void addMoreImages(Product product, int numImages) {

        if (numImages > 0) {
            String imageUrl = product.getImageUrl();
            if (!StringUtils.isEmpty(imageUrl)) {
                imageUrl = imageUrl.substring(0, imageUrl.lastIndexOf("/"))+ "/moreimages/";
            }
            Session session = getSessionFactory().getCurrentSession();
            for (int i=1; i <= numImages; i++) {
                ProductFile productFile = new ProductFile();
                productFile.setFileName(imageUrl + product.getModel() + "_" + i + ".jpg");
                productFile.setFileType("MORE_IMAGE");
                productFile.setProduct(product);
                session.save(productFile);
            }
            //insert one time
            session.flush();
            session.clear();
        }
    }

    @Override
    public void resetDefaultProductImage(Long productId) {
        getSessionFactory().getCurrentSession().createQuery("update ProductFile f set f.default = 'N' where f.product.id = :prodId and f.fileType = 'PRODUCT_FILE_IMAGE'")
                .setParameter("prodId", productId).executeUpdate();
    }

    public ProductFile getProductImage(Long imageId, Long productId) {
        return (ProductFile) getSessionFactory().getCurrentSession().createQuery("select f from ProductFile f where f.product.id = :prodId and f.id = :id")
                .setParameter("prodId", productId)
                .setParameter("id", imageId).uniqueResult();
    }
}