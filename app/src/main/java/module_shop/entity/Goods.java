package module_shop.entity;

import org.litepal.crud.DataSupport;

public class Goods extends DataSupport {
	private String goods_id;
	private String goods_name;
	private String goods_image;
	private int goods_price;
	private String goods_ditail;


	public String getGoods_ditail() {
		return goods_ditail;
	}

	public void setGoods_ditail(String goods_ditail) {
		this.goods_ditail = goods_ditail;
	}

	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getGoods_image() {
		return goods_image;
	}
	public void setGoods_image(String goods_image) {
		this.goods_image = goods_image;
	}
	public int getGoods_price() {
		return goods_price;
	}
	public void setGoods_price(int goods_price) {
		this.goods_price = goods_price;
	}
	@Override
	public String toString() {
		return "Goods [goods_id=" + goods_id + ", goods_name=" + goods_name + ", goods_image=" + goods_image
				+ ", goods_price=" + goods_price + "]";
	}
	
}
