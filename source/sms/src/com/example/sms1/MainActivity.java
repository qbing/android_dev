package com.example.sms1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * 声明变量一个Button与两个EditText
	 */
	private Button mButton1;
	private Button mButton2;
	private EditText mEditText1;
	private EditText mEditText2;
	//private EditText mEditText01;
	private TextView mTextView1;
	private static final int PICK_CONTACT_SUBACTIVITY = 2;
	
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * Called when the activity is first created.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		/*
		 * 通过findViewById构造器来建构
		 * EditText1,EditText2与Button对象
		 */
		mEditText1 = (EditText)findViewById(R.id.myEditText1);
		mEditText2 = (EditText)findViewById(R.id.myEditText2);
		//mEditText01 = (EditText)findViewById(R.id.myEditText01);
		mButton1 = (Button)findViewById(R.id.myButton1);
		mButton2 = (Button)findViewById(R.id.myButton2);
		
		
		//设定onClickListener让用户点击Button时搜索联系人
		mButton2.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(Intent.ACTION_PICK,android.provider.ContactsContract.Contacts.CONTENT_URI),PICK_CONTACT_SUBACTIVITY);
			}
			
		});
		/*
		 * 将默认文字加载到EditText中
		 */
		mEditText1.setText("请输入电话号码");
		mEditText2.setText("请输入短信内容！");
		
		/*
		 * 设置onClickListener让用户单击EditText时作出反应
		 */
		
		
		/*
		 * 从通讯录导入电话号码
		 */
		
		
		
		mEditText1.setOnClickListener(new EditText.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*
				 * 单击EditText时清空正文
				 */
				mEditText1.setText("");
			}
			
		});
		
		/*
		 * 设置onClickListener让用户单击EditText时做出反应
		 */
		mEditText2.setOnClickListener(new EditText.OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 单击EditText时清空正文
				 */
				mEditText2.setText("");
			}
			
			
		});
		
		/*
		 * 设置onClickListener让用户单击Button时作出反应
		 */
		mButton1.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * 由EditText1区的短信收件人电话
				 */
				String strDestAddress = mEditText1.getText().toString();
				/*
				 * 由EditText2取得短信文字内容
				 */
				String strMessage = mEditText2.getText().toString();
				/*
				 * 建构一取得default instance的SmsManager对象
				 */
				
				
				SmsManager smsManager = SmsManager.getDefault();
				
				/*
				 * 检查收件人电话格式与短信字数是否超过70字符
				 */
				if(isPhoneNumberValid(strDestAddress)==true && iswithin70(strMessage)==true)
				{
					try
					{
						/*
						 * 两个条件都检查通过的情况下，发送短信
						 * 先构建一个PendingIntent对象并使用getBroadcast()广播
						 * 将PendingIntent,电话，短信文字等参数
						 * 传入sendTextMessage()方法发送短信
						 */
						
						PendingIntent mPI = PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(), 0);
						smsManager.sendTextMessage(strDestAddress, null, strMessage, mPI, null);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					
					Toast.makeText(MainActivity.this, "送出成功！！", Toast.LENGTH_SHORT).show();
					mEditText1.setText("");
					mEditText2.setText("");
				}
				else
				{
					/*
					 * 电话格式与短信文字不符合条件时，以Toast提醒
					 */
					if(iswithin70(strMessage)==false)
					{
						Toast.makeText(MainActivity.this,"电话号码格式错误+短信内容超过70个字，请检查",Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(MainActivity.this,"电话号码格式错误，请检查！！",Toast.LENGTH_SHORT).show();
						
					}
				}
				/*
				 * 字数超过70个字符
				 */
				 if (iswithin70(strMessage)==false)
				{
					Toast.makeText(MainActivity.this,"短信内容超过70个字，请删除部分内容！！", Toast.LENGTH_SHORT).show();
				}
				
			}
			
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		try
		{	
			switch(requestCode)
			{
			case PICK_CONTACT_SUBACTIVITY:
				final Uri uriRet = data.getData();
				if(uriRet != null)
				{
					try
					{
						//必须要有android.permission.READ_CONTACTS权限
						Cursor c =managedQuery(uriRet, null, null, null, null);
						//将Cursor移到数据最前端
						c.moveToFirst();
						//取得联系人的姓名
						String strName = c.getColumnName(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
						//将姓名写入EditText1中
						//mEditText01.setText(strName);
						//取得联系人的电话
						int contactId = c.getInt(c.getColumnIndex(ContactsContract.Contacts._ID));
						Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = "+contactId, null, null);
						StringBuffer sb = new StringBuffer();
						int typePhone, resType;
						String numPhone;
						if (phones.getCount() > 0)
						{
							phones.moveToFirst();
							//2.0以上可以允许User设置多组电话号码，但本范例只捞一族电话号码作示范
							typePhone = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
							numPhone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							resType = ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(typePhone);
							sb.append(getString(resType)+":"+numPhone+"\n");
							//将电话写入EditText1中
							mEditText1.setText(numPhone);
						}
						else
						{
							sb.append("no Phone number found");
						}
						//Toast是否读取到完整的电话种类与电话号码
						Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
					}
					catch(Exception e)
					{
						//将错误信息在TextView中显示
						mTextView1.setText(e.toString());
						e.printStackTrace();
					}
				}
				break;
				default:
					break;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 * 检查字符串是否为电话号码的方法，并返回true or false的判断值
	 */
	public static boolean isPhoneNumberValid(String phoneNumber)
	{
		boolean isValid = false;
		/*
		 * 可接受的电话格式有：
		 * ^\\(?:可以使用"("作为开头
		 * (\\d{3}):紧接着三个数字
		 * \\)?:可以使用")"继续
		 * [- ]?:在上述格式后可以使用具有选择性的"-"
		 * (\\d{3}):再紧接着三个数字
		 * [- ]?:可以使用具有选择性的"-"继续
		 * (\\d{5})$:以五个数字结束
		 * 可以比较下列数字格式：
		 * (123)456-7890,123-456-7890,1234567890,(123)-456-7890
		 */
		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
		
		String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
		
		CharSequence inputStr = phoneNumber;
		/*
		 * 创建Pattern
		 */
		Pattern pattern = Pattern.compile(expression);
		/*
		 * 将Pattern以参数传入Matcher作Regular expression
		 */
		Matcher matcher = pattern.matcher(inputStr);
		/*
		 * 创建Pattern2
		 */
		Pattern pattern2 = Pattern.compile(expression2);
		/*
		 * 将Pattern2以参数传入Matcher2作Regular expression
		 */
		Matcher matcher2 = pattern2.matcher(inputStr);
		if(matcher.matches()||matcher2.matches())
		{
			isValid = true;
		}
		return isValid;
		
		
	}
	public static boolean iswithin70(String text)
	{
		if (text.length()<= 70)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
