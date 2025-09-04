package cn.xj.kokoro.mobile.ui
/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import androidx.recyclerview.widget.RecyclerView

/**
* Layout that wraps a [RecyclerView] and intercepts touch events to handle nested scrolling
* between a parent [androidx.viewpager2.widget.ViewPager2] and the child [RecyclerView].
*
* This layout should be used as a direct child of [androidx.viewpager2.widget.ViewPager2] and wrap
* the child [RecyclerView] that should be nested scrolled.
*/
