
      <div class="masthead">
          <span class="hidden-print">
              <a href="/"><img style="vertical-align: middle;" src="/images/bookkeeper.jpg" alt="${APP_TITLE}" class="jainTravellerLogoSmall"><sup><b><small>(Beta!)</small></b></sup></a>
              <small><b> स्वागत है ${username!"Guest"}!! </b></small>
              <#if username??>
              | <small><b><a href="/logout">${local["logout"]}</a></b></small> | <small><b><a href="/">मुख पृष्ठ</a></b></small> | <small><b><a href="/lang/hi">हिंदी</a></b></small>  | <small><b><a href="/lang/en">English</a></b></small>
              </#if>
          </span>
          <#if username??>

            <nav class="navbar navbar-default navbar-static-top">
            <ul class="nav nav-tabs">
<#--              <li role="presentation" class="active"><a href="/">${APP_TITLE}</a></li> -->
              <#if admin??>
                  <li role="presentation" class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                      व्यवस्थापक<span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="dLabel">
                        <li><a href="/save">Backup Database</a></li>
                        <li><a href="/restore/backup">Restore Database</a></li>
                    </ul>
                  </li>
              </#if>
              <li role="presentation" class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                  दैनिक सारांश<span class="caret"></span>
                </a>
                <ul class="dropdown-menu" aria-labelledby="dLabel">

                    <li><a href="/last7days">पिछले 7 दिन सारांश</a></li>
                    <li><a href="/last30days">पिछले 30 दिन सारांश</a></li>
                    <li><a href="/quarterly">मासिक सारांश for the तिमाही</a></li>
                </ul>
              </li>
              <li role="presentation" class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                  माल स्टॉक<span class="caret"></span>
                </a>
                <ul class="dropdown-menu" aria-labelledby="dLabel">
                    <li><a href="/inventory/quickSummary">त्वरित स्टॉक सारांश</a></li>
                    <li><a href="/inventory/status/0">स्टॉक Status</a></li>
                    <li><a href="/inventory/markdown/60">स्टॉक Markdown Candidates</a></li>
                    <li class="dropdown-header">माल लेन-देन</li>
                    <#if admin??>
                         <li><a href="/remove/itemTransaction">Remove</a></li>
                    </#if>
                   <li><a href="/list/itemTransaction/100">सूची_Recent100</a></li>
                   <li><a href="/fullList/itemTransaction">सूची</a></li>
                   <li><a href="/list/itemSell/100">सूची_बेचने_Recent100</a></li>
                   <li><a href="/list/itemBuy/100">सूची_खरीदें_Recent100</a></li>
                   <li><a href="/searchEntity/itemTransaction">खोज माल लेन-देन</a></li>
                   <form action="/search" method="POST" class="navbar-form navbar-left" role="search">
                     <div class="form-group">
                         <input type="hidden" name="entity" value="itemTransaction">
                         <input type="text" class="form-control" placeholder="खोज..." name="query"> 
                     </div> 
                     <button type="submit" class="btn btn-default">Submit</button> 
                   </form>
                </ul>
              </li>
              <li role="presentation" class="dropdown">
                  <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false"> स्टॉक Master<span class="caret"></span> </a>
                  <ul class="dropdown-menu" aria-labelledby="dLabel">
                      <li class="dropdown-header">माल Menu</li>
                      <#if trial??>
                      <#else>
                          <li><a href="/add/item">Add</a></li>

                          <#if admin??>
                            <li><a href="/remove/item">Remove</a></li>
                          </#if>
                      </#if>

                    <li><a href="/list/item/100">सूची_Recent100</a></li>
                    <li><a href="/fullList/item">सूची</a></li>
                    <li><a href="/searchEntity/item">खोज माल</a></li>
                      <form action="/search" method="POST" class="navbar-form navbar-left" role="search">
                        <div class="form-group">
                            <input type="hidden" name="entity" value="item">
                            <input type="text" class="form-control" placeholder="खोज..." name="query"> 
                        </div> 
                        <button type="submit" class="btn btn-default">Submit</button> 
                      </form>
                  </ul>
              </li>
              <li role="presentation" class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                  ग्राहक लेन-देन<span class="caret"></span>
                </a>
                <ul class="dropdown-menu" aria-labelledby="dLabel">
                    <#if trial??>
                    <#else>
                    <li><a href="/add/transaction">Add</a></li>

                    <#if admin??>
                    <li><a href="/remove/transaction">Remove</a></li>
                    </#if>
                    </#if>
                    <li><a href="/last7days/transaction">पिछले 7 दिन सारांश</a></li>
                    <li><a href="/last30days/transaction">पिछले 30 दिन सारांश</a></li>
                    <li><a href="/quarterly/transaction">मासिक सारांश for the तिमाही</a></li>

                    <li><a href="/list/transaction/100">सूची_Recent100</a></li>
                    <li><a href="/fullList/transaction">सूची</a></li>
                    <li><a href="/searchEntity/transaction">खोज लेन-देन</a></li>
                    <form action="/search" method="POST" class="navbar-form navbar-left" role="search">
                                <div class="form-group">
                                  <input type="hidden" name="entity" value="transaction">
                                  <input type="text" class="form-control" placeholder="खोज..." name="query">
                                </div>
                                <button type="submit" class="btn btn-default">Submit</button>
                    </form>
                </ul>
              </li>
              <li role="presentation" class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                  ग्राहक Payment<span class="caret"></span>
                </a>
                <ul class="dropdown-menu" aria-labelledby="dLabel">
                    <#if trial??>
                    <#else>
                    <li><a href="/add/payment">Add</a></li>
                    <#if admin??>
                    <li><a href="/remove/payment">Remove</a></li>
                    </#if>
                    </#if>
                    <li><a href="/last7days/payment">पिछले 7 दिन सारांश</a></li>
                    <li><a href="/last30days/payment">पिछले 30 दिन सारांश</a></li>
                    <li><a href="/quarterly/payment">मासिक सारांश for the तिमाही</a></li>

                    <li><a href="/list/payment/100">सूची_Recent100</a></li>
                    <li><a href="/fullList/payment">सूची</a></li>
                    <li><a href="/searchEntity/payment">खोज भुगतान</a></li>
                    <form action="/search" method="POST" class="navbar-form navbar-left" role="search">
                                <div class="form-group">
                                  <input type="hidden" name="entity" value="payment">
                                  <input type="text" class="form-control" placeholder="खोज..." name="query">
                                </div>
                                <button type="submit" class="btn btn-default">खोज</button>
                    </form>
                </ul>
              </li>

              <li role="presentation" class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                  ग्राहक<span class="caret"></span>
                </a>
                <ul class="dropdown-menu" aria-labelledby="dLabel">
                    <#if trial??>
                    <#else>
                    <li><a href="/add/customer">Add</a></li>

                    <#if admin??>
                    <li><a href="/remove/customer">Remove</a></li>
                    </#if>
                    </#if>
                    <li><a href="/list/customer/100">सूची_Recent100</a></li>
                    <li><a href="/fullList/customer">सूची</a></li>
                    <li><a href="/searchEntity/customer">खोजे ग्राहक</a></li>
                    <form action="/search" method="POST" class="navbar-form navbar-left" role="search">
                                <div class="form-group">
                                  <input type="hidden" name="entity" value="customer">
                                  <input type="text" class="form-control" placeholder="खोज..." name="query">
                                </div>
                                <button type="submit" class="btn btn-default">खोज</button>
                    </form>
                </ul>
              </li>

              <li role="presentation" class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                  सेल्समन<span class="caret"></span>
                </a>
                <ul class="dropdown-menu" aria-labelledby="dLabel">
                    <#if trial??>
                    <#else>
                    <li><a href="/add/salesman">Add</a></li>

                    <#if admin??>
                    <li><a href="/remove/salesman">Remove</a></li>
                    </#if>
                    </#if>
                    <li><a href="/list/salesman/100">सूची_Recent100</a></li>
                    <li><a href="/fullList/salesman">सूची</a></li>
                    <li><a href="/searchEntity/salesman">खोजे सेल्समन</a></li>
                    <form action="/search" method="POST" class="navbar-form navbar-left" role="search">
                                <div class="form-group">
                                  <input type="hidden" name="entity" value="salesman">
                                  <input type="text" class="form-control" placeholder="खोज..." name="query">
                                </div>
                                <button type="submit" class="btn btn-default">खोज</button>
                    </form>
                </ul>
              </li>
              <li role="presentation" class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                  कंपनी<span class="caret"></span>
                </a>
                <ul class="dropdown-menu" aria-labelledby="dLabel">
                    <#if trial??>
                    <#else>
                    <li><a href="/add/company">Add</a></li>

                    <#if admin??>
                    <li><a href="/remove/company">Remove</a></li>
                    </#if>
                    </#if>
                    <li><a href="/list/company/100">सूची_Recent100</a></li>
                    <li><a href="/fullList/company">सूची</a></li>
                    <li><a href="/searchEntity/company">खोजे कंपनी</a></li>
                    <form action="/search" method="POST" class="navbar-form navbar-left" role="search">
                                <div class="form-group">
                                  <input type="hidden" name="entity" value="company">
                                  <input type="text" class="form-control" placeholder="खोज..." name="query">
                                </div>
                                <button type="submit" class="btn btn-default">खोज</button>
                    </form>
                </ul>
              </li>


              <form action="/multiSearch" method="POST" class="navbar-form navbar-left" role="search">
                             <div class="form-group">
                             खोजे सब कुछ:  <input type="text" class="form-control" placeholder="सब कुछ खोजे..." name="query">
                             </div>
                             <button type="submit" class="btn btn-default">खोज</button>
              </form>

            </ul>
            </nav>

          <#else>
               <nav>
                  <ul class="nav nav-justified">
                      <li><a href="/">${local["home"]}</a></li>
                      <li><a href="/signup">${local["signup"]}</a></li>
                      <li><a href="/login">${local["login"]}</a></li>
                  </ul>
               </nav>
          </#if>
       </div>
       <#include "/common/alerts.ftl">

<div class="container hidden-print help" style="display:none">
      <br>
      <#include "/common/keyboard_in_IN.ftl">
      <#if username??>
      <table class="table">
              <tr>
              <td>Add ग्राहक लेन-देन: <a href="/add/transaction"><i class="fa fa-plus-square fa-lg" aria-hidden="true"></i></a><span class="tips" style="display:none; color:green " ><b><i> { ctrl t }</i></b></span></td>
              <td>खोजे ग्राहक लेन-देन: <a href="/searchEntity/transaction"><i class="fa fa-search fa-lg" aria-hidden="true"></i></a><span class="tips" style="display:none; color:green " ><b><i> { ctrl shift t }</i></b></span></td>
              </tr>
              <tr>
              <td>Add ग्राहक भुगतान: <a href="/add/payment"><i class="fa fa-plus-square fa-lg" aria-hidden="true"></i></a><span class="tips" style="display:none; color:green  " ><b><i> { ctrl k }</i></b></span></td>
              <td>खोजे ग्राहक भुगतान: <a href="/searchEntity/payment"><i class="fa fa-search fa-lg" aria-hidden="true"></i></a><span class="tips" style="display:none; color:green " ><b><i> { ctrl shift k }</i></b></span></td>
              </tr>
              <tr>

              <td>Add स्टॉक: <a href="/add/item"><i class="fa fa-plus-square fa-lg" aria-hidden="true"></i></a><span class="tips" style="display:none; color:green " ><b><i> { ctrl j }</i></b></span></td>
              <td>खोजे ग्राहक : <a href="/searchEntity/customer"><i class="fa fa-search fa-lg" aria-hidden="true"></i></a><span class="tips" style="display:none;color:green  " ><b><i> { ctrl shift j }</i></b></span></td>
              </tr>
              <tr>
              <td>दैनिक सारांश: <a href="/last7days"><i class="fa fa-line-chart fa-lg" aria-hidden="true"></i></a><span class="tips" style="display:none;color:green  " ><b><i> { ctrl 1 }</i></b></span></td>
              <td>मासिक सारांश: <a href="/quarterly"><i class="fa fa-line-chart fa-lg" aria-hidden="true"></i></a><span class="tips" style="display:none; color:green " ><b><i> { ctrl 2 }</i></b></span></td>
              </tr>
      </table>
      </#if>
  </div>
