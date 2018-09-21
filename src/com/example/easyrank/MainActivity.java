package com.example.easyrank;

import java.util.ArrayList;
import java.util.List;

import noteFragments.NoteFragment;
import userFragments.UserFragment;

import courseFragments.CourseFragment;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnClickListener {

	// ��������
	private ViewPager mViewPager;
	// ViewPager��Ҫ������
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragment;

	private LinearLayout mll_notice;
	private LinearLayout mll_course;
	private LinearLayout mll_user;
	private ImageButton mll_noticeImg;
	private ImageButton mll_courseImg;
	private ImageButton mll_userImg;
	private TextView mll_noticetv;
	private TextView mll_coursetv;
	private TextView mll_usertv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initView();
		initEvents();
		setSelect(1);
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.viewPager);

		mll_notice = (LinearLayout) findViewById(R.id.ll_notice);
		mll_course = (LinearLayout) findViewById(R.id.ll_course);
		mll_user = (LinearLayout) findViewById(R.id.ll_user);

		mll_noticeImg = (ImageButton) findViewById(R.id.ll_notice_img);
		mll_courseImg = (ImageButton) findViewById(R.id.ll_course_img);
		mll_userImg = (ImageButton) findViewById(R.id.ll_user_img);

		mll_noticetv = (TextView) findViewById(R.id.ll_notice_tv);
		mll_coursetv = (TextView) findViewById(R.id.ll_course_tv);
		mll_usertv = (TextView) findViewById(R.id.ll_user_tv);

		mFragment = new ArrayList<Fragment>();
		Fragment mtabnotice = new NoteFragment();
		Fragment mtabcourse = new CourseFragment();
		Fragment mtabuser = new UserFragment();
		mFragment.add(mtabnotice);
		mFragment.add(mtabcourse);
		mFragment.add(mtabuser);

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mFragment.size();// �ж�������
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return mFragment.get(arg0);// ���ز�ͬ��fragment
			}
		};
		 mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				int currentItem = mViewPager.getCurrentItem();
				setTab(currentItem);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void initEvents() {
		// ���ӵ��Ч��
		mll_notice.setOnClickListener(this);
		mll_course.setOnClickListener(this);
		mll_user.setOnClickListener(this);

	}

	private void setSelect(int i) {
		// ����ͼƬΪ��ɫ
		// �л���������
		setTab(i);
		mViewPager.setCurrentItem(i);
	}

	private void setTab(int i) {
		resetImg();
		switch (i) {
		case 0:
			mll_noticeImg.setImageResource(R.drawable.ic_notice_light);
			mll_noticetv.setTextColor(0xff1afa29);
			break;
		case 1:
			mll_courseImg.setImageResource(R.drawable.ic_course_light);
			mll_coursetv.setTextColor(0xff1afa29);
			break;
		case 2:
			mll_userImg.setImageResource(R.drawable.ic_user_light);
			mll_usertv.setTextColor(0xff1afa29);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View v) {
		// ����������������ͼƬ�ȱ䰵
		resetImg();
		switch (v.getId()) {
		case R.id.ll_notice:
			setSelect(0);
			break;
		case R.id.ll_course:
			setSelect(1);
			break;
		case R.id.ll_user:
			setSelect(2);
			break;
		default:
			break;
		}

	}

	private void resetImg() {
		mll_noticeImg.setImageResource(R.drawable.ic_notice);
		mll_noticetv.setTextColor(0xff000000);
		mll_courseImg.setImageResource(R.drawable.ic_course);
		mll_coursetv.setTextColor(0xff000000);
		mll_userImg.setImageResource(R.drawable.ic_user);
		mll_usertv.setTextColor(0xff000000);

	}

}