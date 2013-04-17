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
	 * ��������һ��Button������EditText
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
		 * ͨ��findViewById������������
		 * EditText1,EditText2��Button����
		 */
		mEditText1 = (EditText)findViewById(R.id.myEditText1);
		mEditText2 = (EditText)findViewById(R.id.myEditText2);
		//mEditText01 = (EditText)findViewById(R.id.myEditText01);
		mButton1 = (Button)findViewById(R.id.myButton1);
		mButton2 = (Button)findViewById(R.id.myButton2);
		
		
		//�趨onClickListener���û����Buttonʱ������ϵ��
		mButton2.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(Intent.ACTION_PICK,android.provider.ContactsContract.Contacts.CONTENT_URI),PICK_CONTACT_SUBACTIVITY);
			}
			
		});
		/*
		 * ��Ĭ�����ּ��ص�EditText��
		 */
		mEditText1.setText("������绰����");
		mEditText2.setText("������������ݣ�");
		
		/*
		 * ����onClickListener���û�����EditTextʱ������Ӧ
		 */
		
		
		/*
		 * ��ͨѶ¼����绰����
		 */
		
		
		
		mEditText1.setOnClickListener(new EditText.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/*
				 * ����EditTextʱ�������
				 */
				mEditText1.setText("");
			}
			
		});
		
		/*
		 * ����onClickListener���û�����EditTextʱ������Ӧ
		 */
		mEditText2.setOnClickListener(new EditText.OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ����EditTextʱ�������
				 */
				mEditText2.setText("");
			}
			
			
		});
		
		/*
		 * ����onClickListener���û�����Buttonʱ������Ӧ
		 */
		mButton1.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * ��EditText1���Ķ����ռ��˵绰
				 */
				String strDestAddress = mEditText1.getText().toString();
				/*
				 * ��EditText2ȡ�ö�����������
				 */
				String strMessage = mEditText2.getText().toString();
				/*
				 * ����һȡ��default instance��SmsManager����
				 */
				
				
				SmsManager smsManager = SmsManager.getDefault();
				
				/*
				 * ����ռ��˵绰��ʽ����������Ƿ񳬹�70�ַ�
				 */
				if(isPhoneNumberValid(strDestAddress)==true && iswithin70(strMessage)==true)
				{
					try
					{
						/*
						 * �������������ͨ��������£����Ͷ���
						 * �ȹ���һ��PendingIntent����ʹ��getBroadcast()�㲥
						 * ��PendingIntent,�绰���������ֵȲ���
						 * ����sendTextMessage()�������Ͷ���
						 */
						
						PendingIntent mPI = PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(), 0);
						smsManager.sendTextMessage(strDestAddress, null, strMessage, mPI, null);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					
					Toast.makeText(MainActivity.this, "�ͳ��ɹ�����", Toast.LENGTH_SHORT).show();
					mEditText1.setText("");
					mEditText2.setText("");
				}
				else
				{
					/*
					 * �绰��ʽ��������ֲ���������ʱ����Toast����
					 */
					if(iswithin70(strMessage)==false)
					{
						Toast.makeText(MainActivity.this,"�绰�����ʽ����+�������ݳ���70���֣�����",Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(MainActivity.this,"�绰�����ʽ�������飡��",Toast.LENGTH_SHORT).show();
						
					}
				}
				/*
				 * ��������70���ַ�
				 */
				 if (iswithin70(strMessage)==false)
				{
					Toast.makeText(MainActivity.this,"�������ݳ���70���֣���ɾ���������ݣ���", Toast.LENGTH_SHORT).show();
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
						//����Ҫ��android.permission.READ_CONTACTSȨ��
						Cursor c =managedQuery(uriRet, null, null, null, null);
						//��Cursor�Ƶ�������ǰ��
						c.moveToFirst();
						//ȡ����ϵ�˵�����
						String strName = c.getColumnName(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
						//������д��EditText1��
						//mEditText01.setText(strName);
						//ȡ����ϵ�˵ĵ绰
						int contactId = c.getInt(c.getColumnIndex(ContactsContract.Contacts._ID));
						Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = "+contactId, null, null);
						StringBuffer sb = new StringBuffer();
						int typePhone, resType;
						String numPhone;
						if (phones.getCount() > 0)
						{
							phones.moveToFirst();
							//2.0���Ͽ�������User���ö���绰���룬��������ֻ��һ��绰������ʾ��
							typePhone = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
							numPhone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							resType = ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(typePhone);
							sb.append(getString(resType)+":"+numPhone+"\n");
							//���绰д��EditText1��
							mEditText1.setText(numPhone);
						}
						else
						{
							sb.append("no Phone number found");
						}
						//Toast�Ƿ��ȡ�������ĵ绰������绰����
						Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
					}
					catch(Exception e)
					{
						//��������Ϣ��TextView����ʾ
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
	 * ����ַ����Ƿ�Ϊ�绰����ķ�����������true or false���ж�ֵ
	 */
	public static boolean isPhoneNumberValid(String phoneNumber)
	{
		boolean isValid = false;
		/*
		 * �ɽ��ܵĵ绰��ʽ�У�
		 * ^\\(?:����ʹ��"("��Ϊ��ͷ
		 * (\\d{3}):��������������
		 * \\)?:����ʹ��")"����
		 * [- ]?:��������ʽ�����ʹ�þ���ѡ���Ե�"-"
		 * (\\d{3}):�ٽ�������������
		 * [- ]?:����ʹ�þ���ѡ���Ե�"-"����
		 * (\\d{5})$:��������ֽ���
		 * ���ԱȽ��������ָ�ʽ��
		 * (123)456-7890,123-456-7890,1234567890,(123)-456-7890
		 */
		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
		
		String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
		
		CharSequence inputStr = phoneNumber;
		/*
		 * ����Pattern
		 */
		Pattern pattern = Pattern.compile(expression);
		/*
		 * ��Pattern�Բ�������Matcher��Regular expression
		 */
		Matcher matcher = pattern.matcher(inputStr);
		/*
		 * ����Pattern2
		 */
		Pattern pattern2 = Pattern.compile(expression2);
		/*
		 * ��Pattern2�Բ�������Matcher2��Regular expression
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
