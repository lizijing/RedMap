package models;

/** 
 * @author itzijing@gmail: 
 * @version 创建时间：2012-10-7 下午8:49:21 
 * 类说明 
 */
import javax.persistence.*;
import java.util.*;

import play.*;
import play.db.jpa.*;
import play.libs.*;
import play.data.validation.*;

public class Chart extends Model {

	public String chartPath;

	public Chart(String xmlPath) {
		this.chartPath = xmlPath;
	}

}
