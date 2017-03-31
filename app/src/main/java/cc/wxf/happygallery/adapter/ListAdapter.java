package cc.wxf.happygallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cc.wxf.happygallery.R;
import cc.wxf.happygallery.bean.GalleryPage;
import cc.wxf.happygallery.ui.ImageActivity;

/**
 * Created by chenchen on 2017/3/31.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private Context context;
    private List<GalleryPage> galleryPages;

    public ListAdapter(Context context, List<GalleryPage> galleryPages) {
        this.context = context;
        this.galleryPages = galleryPages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GalleryPage galleryPage = galleryPages.get(position);
        Picasso.with(context).load(galleryPage.getIconUrl()).placeholder(R.drawable.default_icon).error(R.drawable.default_icon).into(holder.img);
        holder.txt.setText(galleryPage.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageActivity.class);
                intent.putExtra("GalleryPage", galleryPage);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryPages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView txt;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            txt = (TextView) itemView.findViewById(R.id.txt);
        }
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            //相当于设置上下左右的padding
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            outRect.top = space;
        }
    }
}
