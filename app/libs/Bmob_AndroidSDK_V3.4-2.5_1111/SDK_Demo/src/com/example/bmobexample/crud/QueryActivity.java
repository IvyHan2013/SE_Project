package com.example.bmobexample.crud;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

import com.example.bmobexample.BaseActivity;
import com.example.bmobexample.R;
import com.example.bmobexample.bean.Person;

/**
  * @ClassName: QueryActivity
  * @Description: ��ѯ����
  * @author smile
  * @date 2014-12-8 ����2:49:57
  */
@SuppressLint("SimpleDateFormat")
public class QueryActivity extends BaseActivity {
	
	protected ListView mListview;
	protected BaseAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_find);
		mListview = (ListView) findViewById(R.id.listview);
		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.tv_item, getResources().getStringArray(
				R.array.bmob_findtest_list));
		mListview.setAdapter(mAdapter);
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				testFind(position + 1);
			}
		});
	}
	
	private void testFind(int pos) {
		switch (pos) {
			case 1:
				queryObject();
//				queryOne();
				break;
			case 2:
				queryObjects();
				break;
			case 3:
				countObjects();
				break;
			case 4:
				compositeAndQuery();
				break;
			case 5:
				compositeOrQuery();
				break;
		}
	}
	
	
	private void queryObjects(){
		final BmobQuery<Person> bmobQuery	 = new BmobQuery<Person>();
		bmobQuery.addWhereEqualTo("age", 25);
		bmobQuery.setLimit(10);
		bmobQuery.order("createdAt");
		//���ж��Ƿ��л���
		boolean isCache = bmobQuery.hasCachedResult(QueryActivity.this,Person.class);
		if(isCache){
			bmobQuery.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);	// �ȴӻ���ȡ���ݣ����û�еĻ����ٴ�����ȡ��
		}else{
			bmobQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);	// ���û�л���Ļ������ȴ�������ȡ
		}
		bmobQuery.findObjects(this, new FindListener<Person>() {

			@Override
			public void onSuccess(List<Person> object) {
				// TODO Auto-generated method stub
				toast("��ѯ�ɹ�����"+object.size()+"�����ݡ�");
				for (Person person : object) {
					Log.d(TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ");
					Log.d(TAG, "ObjectId = "+person.getObjectId());
					Log.d(TAG, "Name = "+person.getName());
					Log.d(TAG, "Age = "+person.getAge());
					Log.d(TAG, "Address = "+person.getAddress());
					Log.d(TAG, "Gender = "+person.isGender());
					Log.d(TAG, "CreatedAt = "+person.getCreatedAt());
					Log.d(TAG, "UpdatedAt = "+person.getUpdatedAt());
				}
			}

			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				toast("��ѯʧ�ܣ�"+msg);
			}
		});
	}
	
	public void queryOne(){
		BmobQuery<Person> query = new BmobQuery<Person>();
		query.getObject(this, "4cab44a404", new GetListener<Person>() {
			
			@Override
			public void onSuccess(Person object) {
				// TODO Auto-generated method stub
				Log.i("life", ""+object.getName());
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				Log.i("life", "onFailure = "+code+",msg = "+msg);
			}
		});
	}
	
	private void queryObject(){
//		BmobQuery<Person> bmobQuery	 = new BmobQuery<Person>();
		BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
		String dateString = "2015-02-11";  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		Date date  = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		bmobQuery.addWhereGreaterThan("createdAt",new BmobDate(date));
		bmobQuery.setLimit(3);
		bmobQuery.order("-createdAt");
//		bmobQuery.addWhereLessThan("createdAt",new BmobDate(date));
//		bmobQuery.addWhereEqualTo("age", 25);
//		bmobQuery.addWhereNotEqualTo("age", 25);
//		bmobQuery.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);	// �ȴӻ���ȡ���ݣ����û�еĻ����ٴ�����ȡ��
		bmobQuery.findObjects(this, new FindListener<Person>() {
			
			@Override
			public void onSuccess(List<Person> objects) {
				// TODO Auto-generated method stub
				if(objects!=null && objects.size()>0){
					Person object = objects.get(0);
					toast("��ѯ�ɹ���"+object.getObjectId());
					Log.d(TAG, "ObjectId = "+object.getObjectId());
					Log.d(TAG, "Name = "+object.getName());
					Log.d(TAG, "Age = "+object.getAge());
					Log.d(TAG, "Address = "+object.getAddress());
					Log.d(TAG, "Gender = "+object.isGender());
					Log.d(TAG, "CreatedAt = "+object.getCreatedAt());
					Log.d(TAG, "UpdatedAt = "+object.getUpdatedAt());
				}
			}

			@Override
			public void onError(int code, String arg0) {
				// TODO Auto-generated method stub
				toast("��ѯʧ�ܣ�"+arg0);
			}

		});
	}
	
	private void countObjects(){
		BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
		bmobQuery.count(this, Person.class, new CountListener() {
			
			@Override
			public void onSuccess(int count) {
				// TODO Auto-generated method stub
				toast("count�������Ϊ��"+count);
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				toast("count�������ʧ�ܣ�"+msg);
			}
		});
	}
	
	/**
	  * @Description: �������ѯ����ѯ����6-29��֮�䣬��������"y"����"e"��β����
	  * @param  
	  * @return void
	  * @throws
	  */
	private void compositeAndQuery(){
		//��ѯ����6-29��֮����ˣ�ÿһ����ѯ��������Ҫnewһ��BmobQuery����
		//--and����1
		BmobQuery<Person> eq1 = new BmobQuery<Person>();
		eq1.addWhereLessThanOrEqualTo("age", 29);//����<=29
		//--and����2
		BmobQuery<Person> eq2 = new BmobQuery<Person>();
		eq2.addWhereGreaterThanOrEqualTo("age", 6);//����>=6

		//��ѯ������"y"����"e"��β����--�����Ҫʹ�õ�or��ѯ
		//--and����3
		BmobQuery<Person> eq3 = new BmobQuery<Person>();
		eq3.addWhereEndsWith("name", "y");
		BmobQuery<Person> eq4 = new BmobQuery<Person>();
		eq4.addWhereEndsWith("name", "e");
		List<BmobQuery<Person>> queries = new ArrayList<BmobQuery<Person>>();
		queries.add(eq3);
		queries.add(eq4);
		BmobQuery<Person> mainQuery = new BmobQuery<Person>();
		BmobQuery<Person> or = mainQuery.or(queries);

		//�����װ������and����
		List<BmobQuery<Person>> andQuerys = new ArrayList<BmobQuery<Person>>();
		andQuerys.add(eq1);
		andQuerys.add(eq2);
		andQuerys.add(or);
		//��ѯ��������and��������
		BmobQuery<Person> query = new BmobQuery<Person>();
		query.and(andQuerys);
		query.findObjects(this, new FindListener<Person>() {
		    @Override
		    public void onSuccess(List<Person> object) {
		        // TODO Auto-generated method stub
		    	toast("��ѯ����6-29��֮�䣬������'y'����'e'��β���˸�����"+object.size());
		    }
		    @Override
		    public void onError(int code, String msg) {
		        // TODO Auto-generated method stub
		    	toast("���ϲ�ѯʧ�ܣ�"+code+",msg:"+msg);
		    }
		    
		});
	}
	
	/**
	 * @Description: ���ϻ��ѯ����ѯname�ֶ���ֵ�Ҳ�Ϊ"",��ѯage ���� 29 ���� age ���� 6 ����
	 * @param  
	 * @return void
	 * @throws
	 */
	private void compositeOrQuery(){
		BmobQuery<Person> and1 = new BmobQuery<Person>();
		and1.addWhereExists("name");
		BmobQuery<Person> and2 = new BmobQuery<Person>();
		and2.addWhereNotEqualTo("name", "");
		List<BmobQuery<Person>> ands = new ArrayList<BmobQuery<Person>>();
		ands.add(and1);
		ands.add(and2);
		
		BmobQuery<Person> or1 = new BmobQuery<Person>();
		or1.addWhereEqualTo("age", 6);
		BmobQuery<Person> or2 = new BmobQuery<Person>();
		or2.addWhereEqualTo("age", 29);
		List<BmobQuery<Person>> ors = new ArrayList<BmobQuery<Person>>();
		ors.add(or1);
		ors.add(or2);
		
		BmobQuery<Person> mainQuery = new BmobQuery<Person>();
		mainQuery.order("-createdAt");
		mainQuery.and(ands);
		mainQuery.or(ors);
		mainQuery.findObjects(this, new FindListener<Person>() {
			@Override
			public void onSuccess(List<Person> object) {
				// TODO Auto-generated method stub
				toast("����Ϊ29����6���˵ĸ�����"+object.size());
			}
			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				toast("���ϲ�ѯʧ�ܣ�"+code+",msg:"+msg);
			}
		});
	}

}