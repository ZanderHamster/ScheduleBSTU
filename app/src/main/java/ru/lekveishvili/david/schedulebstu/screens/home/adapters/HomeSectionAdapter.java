package ru.lekveishvili.david.schedulebstu.screens.home.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import ru.lekveishvili.david.schedulebstu.R;
import ru.lekveishvili.david.schedulebstu.models.Event;

public class HomeSectionAdapter extends StatelessSection {
    private String header;
    private List<Event> eventList;
    public ItemClickListener onItemClickListener;

    public HomeSectionAdapter(String header, List<Event> eventList) {
        super(R.layout.day_section_header, R.layout.day_section_item);
        this.header = header;
        this.eventList = eventList;
    }

    @Override
    public int getContentItemsTotal() {
        return eventList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        headerHolder.dayHeader.setText(header);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        Event event = eventList.get(position);

        String eventType = event.getEventType().getName();
        String subject = event.getSubject().getName();
        String teacher = "";
        for (int i = 0; i < event.getTeachers().size(); i++) {
            String firstName = "";
            String secondName = "";
            String thirdName = "";
            if (event.getTeachers().get(i).getFirstName() != null) {
                firstName = event.getTeachers().get(i).getFirstName() + " ";
            }
            if (event.getTeachers().get(i).getSecondName() != null) {
                secondName = String.valueOf(event.getTeachers().get(i).getSecondName().charAt(0)) + ". ";
            }
            if (event.getTeachers().get(i).getThirdName() != null) {
                thirdName = String.valueOf(event.getTeachers().get(i).getThirdName().charAt(0)) + ". ";
            }
            teacher += firstName + secondName + thirdName;
        }
        String group = "";
        for (int i = 0; i < event.getGroups().size(); i++) {
            group += event.getGroups().get(i).getName() + " ";
        }

        String room = event.getRoom().getName();
        DateFormat df = new SimpleDateFormat("HH:mm", new Locale("ru", "RU"));
        String startEvent = df.format(event.getStartEvent());
        String endEvent = df.format(event.getEndEvent());


        itemHolder.eventType.setText(eventType);
        itemHolder.subject.setText(subject);
        itemHolder.teacher.setText(teacher);
        itemHolder.room.setText(room);
        itemHolder.group.setText(group);
        itemHolder.startEvent.setText(startEvent);
        itemHolder.endEvent.setText(endEvent);
        if (position == eventList.size() - 1) {
            itemHolder.borderLine.setVisibility(View.GONE);
            itemHolder.layout.setBackground(itemHolder.layout.getResources().getDrawable(R.drawable.day_bottom));
        }
        // Нажатие на жлемент списка
        itemHolder.layout.setOnClickListener(v -> onItemClickListener.onClick(event));
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface ItemClickListener {
        void onClick(Event event);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.day_section_item_start_event)
        TextView startEvent;
        @BindView(R.id.day_section_item_end_event)
        TextView endEvent;
        @BindView(R.id.day_section_item_event_type)
        TextView eventType;
        @BindView(R.id.day_section_item_subject)
        TextView subject;
        @BindView(R.id.day_section_item_teacher)
        TextView teacher;
        @BindView(R.id.day_section_item_room)
        TextView room;
        @BindView(R.id.day_section_item_group)
        TextView group;
        @BindView(R.id.day_section_item_border)
        ImageView borderLine;
        @BindView(R.id.day_section_item_layout)
        RelativeLayout layout;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.day_section_header_header)
        TextView dayHeader;

        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
