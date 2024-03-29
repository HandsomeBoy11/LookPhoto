package com.xrd.lookphoto;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.ielse.imagewatcher.ImageWatcher;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MessagePicturesLayout.Callback, ImageWatcher.OnPictureLongPressListener{
    private ImageWatcher vImageWatcher;

    private RecyclerView vRecycler;
    private MessageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isTranslucentStatus = false;
        setContentView(R.layout.activity_main);

        findViewById(R.id.vRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext().getApplicationContext(), "刷新列表内容 adapter.reset()", Toast.LENGTH_SHORT).show();
                adapter.set(Data.get());
            }
        });

        vRecycler = (RecyclerView) findViewById(R.id.v_recycler);
        vRecycler.setLayoutManager(new LinearLayoutManager(this));
        vRecycler.addItemDecoration(new SpaceItemDecoration(this).setSpace(14).setSpaceColor(0xFFECECEC));
        vRecycler.setAdapter(adapter = new MessageAdapter(this).setPictureClickCallback(this));
        adapter.set(Data.get());

        // **************   xml 方式加载  ********  推荐使用后面demo的iwHelper

        // 一般来讲， ImageWatcher 需要占据全屏的位置
        vImageWatcher = (ImageWatcher) findViewById(R.id.v_image_watcher);
        // 如果不是透明状态栏，你需要给ImageWatcher标记 一个偏移值，以修正点击ImageView查看的启动动画的Y轴起点的不正确
        vImageWatcher.setTranslucentStatus(!isTranslucentStatus ? Utils.calcStatusBarHeight(this) : 0);
        // 配置error图标 如果不介意使用lib自带的图标，并不一定要调用这个API
        vImageWatcher.setErrorImageRes(R.mipmap.error_picture);
        // 长按图片的回调，你可以显示一个框继续提供一些复制，发送等功能
        vImageWatcher.setOnPictureLongPressListener(this);
        vImageWatcher.setLoader(new GlideSimpleLoader());
        vImageWatcher.addOnStateChangedListener(new ImageWatcher.OnStateChangedListener() {
            @Override
            public void onStateChangeUpdate(ImageWatcher imageWatcher, ImageView clicked, int position, Uri uri, float animatedValue, int actionTag) {
                Log.e("IW", "onStateChangeUpdate [" + position + "][" + uri + "][" + animatedValue + "][" + actionTag + "]");
            }

            @Override
            public void onStateChanged(ImageWatcher imageWatcher, int position, Uri uri, int actionTag) {
                if (actionTag == ImageWatcher.STATE_ENTER_DISPLAYING) {
                    Toast.makeText(getApplicationContext(), "点击了图片 [" + position + "]" + uri + "", Toast.LENGTH_SHORT).show();
                } else if (actionTag == ImageWatcher.STATE_EXIT_HIDING) {
                    Toast.makeText(getApplicationContext(), "退出了查看大图", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Utils.fitsSystemWindows(isTranslucentStatus, findViewById(R.id.v_fit));
    }
    @Override
    public void onThumbPictureClick(ImageView i, SparseArray<ImageView> imageGroupList, List<Uri> urlList) {
        vImageWatcher.show(i, imageGroupList, urlList);
    }

    @Override
    public void onPictureLongPress(ImageView v, Uri uri, int pos) {
        Toast.makeText(v.getContext().getApplicationContext(), "长按了第" + (pos + 1) + "张图片", Toast.LENGTH_SHORT).show();

        new SheetDialog.Builder(this)
                .addSheet("发送给好友", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .addSheet("转载到空间相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .addSheet("保存到手机", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .addSheet("收藏", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    @Override
    public void onBackPressed() {
        // 没有使用helper初始化
        if (!vImageWatcher.handleBackPressed()) {
            super.onBackPressed();
        }
    }
}
