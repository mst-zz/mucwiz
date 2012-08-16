package com.mucwiz;

import java.util.List;

import com.mucwiz.model.Question;
 
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 
public class QListAdapter extends BaseAdapter {
    private static List<Question> qList;
 
    private LayoutInflater mInflater;
 
    public QListAdapter(Context context, List<Question> qList1) {
        qList = qList1;
        mInflater = LayoutInflater.from(context);
    }
 
    public int getCount() {
        return qList.size();
    }
 
    public Object getItem(int position) {
        return qList.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_q_list_item, null);
            holder = new ViewHolder();
            holder.question = (TextView) convertView.findViewById(R.id.question);
            holder.track = (TextView) convertView.findViewById(R.id.question_track);
            holder.a1 = (TextView) convertView.findViewById(R.id.a1);
            holder.a2 = (TextView) convertView.findViewById(R.id.a2);
            holder.a3 = (TextView) convertView.findViewById(R.id.a3);
            holder.a4 = (TextView) convertView.findViewById(R.id.a4);
            holder.a5 = (TextView) convertView.findViewById(R.id.a5);
 
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        String qText = "Question " + (position+1) + ": ";
        if (qList.get(position).getQType().equals("artist"))
        	qText += "Which artist is this?";
        else
        	qText += "Which song is this?";
        holder.question.setText(qText);
        holder.track.setText(qList.get(position).getTrack());
        holder.a1.setText(qList.get(position).getAlternatives().get(0));
        holder.a2.setText(qList.get(position).getAlternatives().get(1));
        holder.a3.setText(qList.get(position).getAlternatives().get(2));
        holder.a4.setText(qList.get(position).getAlternatives().get(3));
        holder.a5.setText(qList.get(position).getAlternatives().get(4));
 
        return convertView;
    }
 
    static class ViewHolder {
        TextView question;
        TextView track;
        TextView a1;
        TextView a2;
        TextView a3;
        TextView a4;
        TextView a5;
    }
}