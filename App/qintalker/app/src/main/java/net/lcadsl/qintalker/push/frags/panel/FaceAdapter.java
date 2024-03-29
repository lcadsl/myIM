package net.lcadsl.qintalker.push.frags.panel;

import android.view.View;

import net.lcadsl.qintalker.common.widget.recycler.RecyclerAdapter;
import net.lcadsl.qintalker.face.Face;
import net.lcadsl.qintalker.push.R;

import java.util.List;

/**

 */
public class FaceAdapter extends RecyclerAdapter<Face.Bean> {

    public FaceAdapter(List<Face.Bean> beans, AdapterListener<Face.Bean> listener) {
        super(beans, listener);
    }

    @Override
    protected int getItemViewType(int position, Face.Bean bean) {
        return R.layout.cell_face;
    }

    @Override
    protected ViewHolder<Face.Bean> onCreateViewHolder(View root, int viewType) {
        return new FaceHolder(root);
    }
}
