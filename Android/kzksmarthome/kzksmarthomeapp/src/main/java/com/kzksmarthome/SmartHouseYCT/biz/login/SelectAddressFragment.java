package com.kzksmarthome.SmartHouseYCT.biz.login;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.BindView;

import com.kzksmarthome.SmartHouseYCT.R;
import com.kzksmarthome.SmartHouseYCT.biz.base.BaseFragment;
import com.kzksmarthome.SmartHouseYCT.biz.event.EventOfResultAddress;
import com.kzksmarthome.SmartHouseYCT.model.CityModel;
import com.kzksmarthome.SmartHouseYCT.model.DistrictModel;
import com.kzksmarthome.SmartHouseYCT.model.ProvinceModel;
import com.kzksmarthome.SmartHouseYCT.util.XmlParserHandler;
import com.kzksmarthome.SmartHouseYCT.widget.wheel.OnWheelChangedListener;
import com.kzksmarthome.SmartHouseYCT.widget.wheel.WheelView;
import com.kzksmarthome.SmartHouseYCT.widget.wheel.adapters.ArrayWheelAdapter;
import com.kzksmarthome.common.lib.eventbus.GjjEventBus;
import com.kzksmarthome.common.lib.task.ForegroundTaskExecutor;
import com.kzksmarthome.common.lib.util.Util;
import com.kzksmarthome.common.module.log.L;

/**
 * 
 * @Title:
 * @Description: 地址选择界面
 * @author jack
 * @date 2015年7月21日 下午9:50:50
 * @version V1.0
 */
public class SelectAddressFragment extends BaseFragment implements OnWheelChangedListener {
    /**
	 * 
	 */
    protected String[] mProvinceDatas;
    /**
	 * 
	 */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
	 * 
	 */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
	 * 
	 */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
	 * 
	 */
    protected String mCurrentProvinceName = "";
    /**
	 * 
	 */
    protected String mCurrentCityName = "";
    /**
	 * 
	 */
    protected String mCurrentDistrictName = "";

    /**
	 * 
	 */
    protected String mCurrentZipCode = "";
    /**
     * 省
     */
    @BindView(R.id.id_province)
    WheelView mViewProvince;
    /**
     * 市
     */
    @BindView(R.id.id_city)
    WheelView mViewCity;
    /**
     * 县区
     */
    @BindView(R.id.id_district)
    WheelView mViewDistrict;
    /**
     * 小区
     */
    @BindView(R.id.id_home_number)
    EditText id_home_number;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_select_address, container, false);
        ButterKnife.bind(this, mRootView);
        setUpListener();
        setUpData();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
   
    

    /**
     * 设置滑动监听
     */
    private void setUpListener() {
        mViewProvince.addChangingListener(this);
        mViewCity.addChangingListener(this);
        mViewDistrict.addChangingListener(this);
    }
    /**
     * 设置数据
     */
    private void setUpData() {
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), new String[]{""}));
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), new String[]{""}));
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), new String[]{""}));
        ForegroundTaskExecutor.executeTask(new Runnable() {
            @Override
            public void run() {
                initProvinceDatas();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getActivity() == null) {
                            return;
                        }
                        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), mProvinceDatas));
//                        ((ArrayWheelAdapter)mViewProvince.getViewAdapter()).updateData(mProvinceDatas);
                        mViewProvince.setVisibleItems(7);
                        mViewCity.setVisibleItems(7);
                        mViewDistrict.setVisibleItems(7);
                        updateCities();
                        updateAreas();
                    }
                });
            }
        });
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            updateCounty();
        }
    }

    private void updateCounty() {
        int pCurrent = mViewDistrict.getCurrentItem();
        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[pCurrent];
        mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
    }

    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProvinceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[] { "" };
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), areas));
//        ((ArrayWheelAdapter)mViewDistrict.getViewAdapter()).updateData(areas);
        mViewDistrict.setCurrentItem(0);
        updateCounty();
    }

    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProvinceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProvinceName);
        if (cities == null) {
            cities = new String[] { "" };
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), cities));
//        ((ArrayWheelAdapter)mViewCity.getViewAdapter()).updateData(cities);
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getActivity().getAssets();
        InputStream input = null;
        try {
            input = asset.open("chinaaddress.xml");

            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            provinceList = handler.getDataList();
            if (provinceList != null && !provinceList.isEmpty()) {
                ProvinceModel pm = provinceList.get(0);
                mCurrentProvinceName = pm.getName();
                List<CityModel> cityList = pm.getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    CityModel cm = cityList.get(0);
                    mCurrentCityName = cm.getName();
                    List<DistrictModel> districtList = cm.getDistrictList();
                    DistrictModel dm = districtList.get(0);
                    mCurrentDistrictName = dm.getName();
                    mCurrentZipCode = dm.getZipcode();
                }
            }
            // */
            int provinceSize = provinceList.size();
            mProvinceDatas = new String[provinceSize];
            for (int i = 0; i < provinceSize; i++) {
                ProvinceModel pm = provinceList.get(i);
                mProvinceDatas[i] = pm.getName();
                List<CityModel> cityList = pm.getCityList();
                int citySize = cityList.size();
                String[] cityNames = new String[citySize];
                for (int j = 0; j < citySize; j++) {
                    CityModel cm = cityList.get(j);
                    cityNames[j] = cm.getName();
                    List<DistrictModel> districtList = cm.getDistrictList();
                    int districtSize = districtList.size();
                    String[] districtNameArray = new String[districtSize];
//                    DistrictModel[] districtArray = new DistrictModel[districtSize];
                    for (int k = 0; k < districtSize; k++) {
                        DistrictModel dm = districtList.get(k);
                        mZipcodeDatasMap.put(dm.getName(), dm.getZipcode());
//                        districtArray[k] = dm;
                        districtNameArray[k] = dm.getName();
                    }
                    mDistrictDatasMap.put(cityNames[j], districtNameArray);
                }
                mCitisDatasMap.put(pm.getName(), cityNames);
            }
        } catch (Throwable e) {
            L.e(e);
        } finally {
            Util.closeCloseable(input);
        }
    }
    @Override
    public void onRightBtnClick() {
        String addressStr = "";
        if (mCurrentProvinceName.equals(mCurrentCityName) && mCurrentCityName.equals(mCurrentDistrictName)) {
            addressStr = mCurrentDistrictName;
        } else if (mCurrentProvinceName.equals(mCurrentCityName) && !mCurrentCityName.equals(mCurrentDistrictName)) {
            addressStr = mCurrentCityName + mCurrentDistrictName;
        } else if (!mCurrentProvinceName.equals(mCurrentCityName) && mCurrentCityName.equals(mCurrentDistrictName)) {
            addressStr = mCurrentProvinceName + mCurrentDistrictName;
        } else {
            addressStr = mCurrentProvinceName + mCurrentCityName + mCurrentDistrictName;
        }
        String addressId= mCurrentZipCode;
        if (!TextUtils.isEmpty(addressStr) && !TextUtils.isEmpty(addressId)) {
            EventOfResultAddress resultAddress = new EventOfResultAddress();
            resultAddress.AddressStr = addressStr;
            resultAddress.AddressId = addressId;
            String addressXQ = id_home_number.getText().toString().trim();
            if (!TextUtils.isEmpty(addressXQ)) {
                resultAddress.AddressXQ = addressXQ;
                resultAddress.AddressStr =  resultAddress.AddressStr + addressXQ;
            }
            GjjEventBus.getInstance().post(resultAddress);
        }
        onBackPressed();
    }
}
