package rubado.printeasy.adapters;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import rubado.printeasy.Pojos.PaymentPojo;
import rubado.printeasy.R;

/**
 * Created by Sol Rubado on 10/04/2017.
 */

public class PaymentAdapter extends BaseAdapter {

    private static final String TAG = FileRowAdapter.class.getSimpleName();
    private final Context mContext;
    private final Application mApplication;
    private final ArrayList<PaymentPojo> mPaymentList;
    private final LayoutInflater mInflater;

    public PaymentAdapter(Context context, Application application, ArrayList<PaymentPojo> paymentList) {
        mContext = context;
        mApplication = application;
        mPaymentList = paymentList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mPaymentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPaymentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MyViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.payment_row, null);
            viewHolder = new MyViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }

        viewHolder.fileName= (TextView) convertView.findViewById(R.id.filenamePrice);
        viewHolder.price = (TextView) convertView.findViewById(R.id.price);

        if (mPaymentList.size() > position) {
            viewHolder.fileName.setText(mPaymentList.get(position).getFilePrinted());
            viewHolder.price.setText("$"+mPaymentList.get(position).getPrice());

        }

        //handleButtonClick(convertView, position);

        return convertView;
    }


    private class MyViewHolder {
        TextView fileName;
        TextView price;
    }
}
