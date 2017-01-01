【前言：】
一个RecyclerView的Item加载是有顺序的，类似于Activity的生命周期（姑且这么叫）
，具体可以对adapter的每个方法进行重写打下日志进行查看，具体大致为：

1、getItemViewType(获取显示类型，返回值可在onCreateViewHolder中拿到，以决定加载哪种ViewHolder)

2、onCreateViewHolder(加载ViewHolder的布局)

3、onViewAttachedToWindow（当Item进入这个页面的时候调用）

4、onBindViewHolder(将数据绑定到布局上，以及一些逻辑的控制就写这啦)

5、onViewDetachedFromWindow（当Item离开这个页面的时候调用）

6、onViewRecycled(当Item被回收的时候调用)