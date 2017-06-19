/*
 * Copyright (c) 2016 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.eusecom.attendance;

import android.util.Log;
import com.eusecom.attendance.models.Attendance;
import java.util.ArrayList;
import java.util.List;

public class AbsServerSearchEngine {

  private final List<Attendance> mListabsserver;
  private final int mListabsserverCount;

  public AbsServerSearchEngine(List<Attendance> listabsserver) {
    mListabsserver = listabsserver;
    mListabsserverCount = mListabsserver.size();
  }


  public List<Attendance> searchModel(String query) {
    query = query.toLowerCase();
    Log.d("searchModel", query);

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    List<Attendance> resultAs = new ArrayList<Attendance>();

    for (int i = 0; i < mListabsserverCount; i++) {
      if (mListabsserver.get(i).dmna.toLowerCase().contains(query)) {
        resultAs.add(mListabsserver.get(i));
        Log.d("mListabs.get(i).dmna", mListabsserver.get(i).dmna);
      }
    }

    return resultAs;
  }

}
