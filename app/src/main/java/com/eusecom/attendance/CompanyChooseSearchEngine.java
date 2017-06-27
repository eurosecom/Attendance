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
import com.eusecom.attendance.models.Company;
import java.util.ArrayList;
import java.util.List;

public class CompanyChooseSearchEngine {

  private final List<Company> mListcompany;
  private final int mListcompanyCount;

  public CompanyChooseSearchEngine(List<Company> listcompany) {
    mListcompany = listcompany;
    mListcompanyCount = mListcompany.size();
  }


  public List<Company> searchModel(String query) {
    query = query.toLowerCase();
    Log.d("searchModel", query);

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    List<Company> resultAs = new ArrayList<Company>();

    for (int i = 0; i < mListcompanyCount; i++) {
      if (mListcompany.get(i).cmname.toLowerCase().contains(query)) {
        resultAs.add(mListcompany.get(i));
      }
    }

    return resultAs;
  }

}
