package rubado.printeasy.adapters;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import rubado.printeasy.Pojos.FilePojo;
import rubado.printeasy.R;
import rubado.printeasy.activities.MainActivity;

/**
 * Created by MariaSol on 06/09/2016.
 */
public class FileRowAdapter extends BaseAdapter {

    private static final String TAG = FileRowAdapter.class.getSimpleName();
    private final Context mContext;
    private final Application mApplication;
    private final ArrayList<FilePojo> mFileList;
    private final LayoutInflater mInflater;

    public FileRowAdapter(Context context, Application application, ArrayList<FilePojo> fileList) {
        mContext = context;
        mApplication = application;
        mFileList = fileList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mFileList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MyViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.file_row, null);
            viewHolder = new MyViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }

        viewHolder.fileName= (TextView) convertView.findViewById(R.id.fileName);
        viewHolder.deleteBtn = (ImageView) convertView.findViewById(R.id.deleteBtn);

        if (mFileList.size() > position) {
            viewHolder.fileName.setText(mFileList.get(position).getFileName());

        }

        handleButtonClick(convertView, position);

        return convertView;
    }


    private void handleButtonClick(View convertView, final int position) {
        ImageView deleteBtn = (ImageView) convertView.findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mContext).deleteDocument(mFileList.get(position).getId());

            }
        });
    }

    private class MyViewHolder {
        TextView fileName;
        ImageView deleteBtn;
    }
}
