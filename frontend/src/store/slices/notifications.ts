import {createSlice, PayloadAction} from "@reduxjs/toolkit";

type NotificationsState = {
    subscriptionId?: string;
}

const initialState: NotificationsState = {
    subscriptionId: undefined
}

export const notificationsSlice = createSlice({
    name: "notifications",
    initialState,
    reducers: {
        subscribedToNotifications: (state, action: PayloadAction<string>) => {
            state.subscriptionId = action.payload;
        },
        unsubscribedFromNotifications: (state) => {
            state.subscriptionId = undefined;
        }
    }
})

export const {
    subscribedToNotifications,
    unsubscribedFromNotifications,
} = notificationsSlice.actions;

export default notificationsSlice.reducer;
