package com.example.fragment_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeController extends ItemTouchHelper.Callback {

    private Bitmap editIcon, deleteIcon;

    public SwipeController(Context context) {
        // 加载图标资源
        editIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.edit_pencil);  // 替换为实际的编辑图标文件名
        deleteIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.trash_can);  // 替换为实际的删除图标文件名

        if (editIcon == null || deleteIcon == null) {
            Log.e("SwipeController", "Icon failed to load.");
        }
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // 不进行操作
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        float buttonWidth = 435f;
        if (dX < -buttonWidth) {
            dX = -buttonWidth;
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX <= -buttonWidth) {
            View itemView = viewHolder.itemView;

            Paint paint = new Paint();

            // 绘制编辑按钮背景和图标
            paint.setColor(Color.YELLOW);
            RectF editButton = new RectF(itemView.getRight() + dX, itemView.getTop(),
                    itemView.getRight() + dX + buttonWidth / 2, itemView.getBottom());
            c.drawRect(editButton, paint);
            if (editIcon != null) {
                drawIcon(editIcon, c, editButton);
            }

            // 绘制删除按钮背景和图标
            paint.setColor(Color.RED);
            RectF deleteButton = new RectF(itemView.getRight() + dX + buttonWidth / 2,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
            c.drawRect(deleteButton, paint);
            if (deleteIcon != null) {
                drawIcon(deleteIcon, c, deleteButton);
            }
        }
    }

    private void drawIcon(Bitmap icon, Canvas c, RectF button) {
        // 缩放图标以适应按钮区域
        int iconSize = (int) (button.height() * 0.5);  // 图标大小为按钮高度的 50%
        Bitmap scaledIcon = Bitmap.createScaledBitmap(icon, iconSize, iconSize, false);

        // 将图标绘制在按钮中心位置
        float left = button.centerX() - (scaledIcon.getWidth() / 2);
        float top = button.centerY() - (scaledIcon.getHeight() / 2);
        c.drawBitmap(scaledIcon, left, top, null);
    }
}
