
      <div class="masthead">
          <span class="hidden-print">
              <a href="/"><img style="vertical-align: middle;" src="/images/bookkeeper.jpg" alt="${APP_TITLE}" class="jainTravellerLogoSmall"><sup><b><small>(Beta!)</small></b></sup></a>
              <small><b> Welcome ${username!"Guest"}!! </b></small>
              <#if username??>
              | <small><b><a href="/logout">${local["logout"]}</a></b></small> | <small><b><a href="/">${local["home"]}</a></b></small> | <small><b><a href="/lang/hi">हिंदी</a></b></small>  | <small><b><a href="/lang/en">English</a></b></small>
              </#if>
          </span>
          <#if username??>

            <nav class="navbar navbar-default navbar-static-top">
            <ul class="nav nav-tabs">
<#--              <li role="presentation" class="active"><a href="/">${APP_TITLE}</a></li> -->
              <#if admin??>
                  <li role="presentation" class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                      Admin<span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="dLabel">
                        <li><a href="/save">Backup Database</a></li>
                        <li><a href="/restore/backup">Restore Database</a></li>
                    </ul>
                  </li>
              </#if>
              <li role="presentation" class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                  DailySummary<span class="caret"></span>
                </a>
                <ul class="dropdown-menu" aria-labelledby="dLabel">

                    <li><a href="/last7days">Last 7 days Summary</a></li>
                    <li><a href="/last30days">Last 30 days Summary</a></li>
                    <li><a href="/quarterly">Monthly Summary for the Quarter</a></li>
                </ul>
              </li>
              <li role="presentation" class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                  ItemInventory<span class="caret"></span>
                </a>
                <ul class="dropdown-menu" aria-labelledby="dLabel">
                    <li><a href="/inventory/quickSummary">InventoryQuickSummary</a></li>
                    <li><a href="/inventory/status/0">InventoryStatus</a></li>
                    <li><a href="/inventory/markdown/60">InventoryMarkdownCandidates</a></li>
                    <li class="dropdown-header">Item Transactions</li>
                    <#if admin??>
                         <li><a href="/remove/itemTransaction">Remove</a></li>
                    </#if>
                   <li><a href="/list/itemTransaction/100">List_Recent100</a></li>
                   <li><a href="/fullList/itemTransaction">List</a></li>
                   <li><a href="/list/itemSell/100">List_SELL_Recent100</a></li>
                   <li><a href="/list/itemBuy/100">List_BUY_Recent100</a></li>
                   <li><a href="/searchEntity/itemTransaction">Search ItemTransaction</a></li>
                   <form action="/search" method="POST" class="navbar-form navbar-left" role="search">
                     <div class="form-group">
                         <input type="hidden" name="entity" value="itemTransaction">
                         <input type="text" class="form-control" placeholder="search..." name="query"> 
                     </div> 
                     <button type="submit" class="btn btn-default">Submit</button> 
                   </form>
                </ul>
              </li>
              <li role="presentation" class="dropdown">
                  <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false"> Inventory Master<span class="caret"></span> </a>
                  <ul class="dropdown-menu" aria-labelledby="dLabel">
                      <li class="dropdown-header">Item Menu</li>
                      <#if trial??>
                      <#else>
                          <li><a href="/add/item">Add</a></li>

                          <#if admin??>
                            <li><a href="/remove/item">Remove</a></li>
                          </#if>
                      </#if>

                    <li><a href="/list/item/100">List_Recent100</a></li>
                    <li><a href="/fullList/item">List</a></li>
                    <li><a href="/searchEntity/item">Search Item</a></li>
                      <form action="/search" method="POST" class="navbar-form navbar-left" role="search">
                        <div class="form-group">
                            <input type="hidden" name="entity" value="item">
                            <input type="text" class="form-control" placeholder="search..." name="query"> 
                        </div> 
                        <button type="submit" class="btn btn-default">Submit</button> 
                      </form>
                  </ul>
              </li>
              <li role="presentation" class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                  Customer Transaction<span class="caret"></span>
                </a>
                <ul class="dropdown-menu" aria-labelledby="dLabel">
                    <#if trial??>
                    <#else>
                    <li><a href="/add/transaction">Add</a></li>

                    <#if admin??>
                    <li><a href="/remove/transaction">Remove</a></li>
                    </#if>
                    </#if>
                    <li><a href="/last7days/transaction">Last 7 days Summary</a></li>
                    <li><a href="/last30days/transaction">Last 30 days Summary</a></li>
                    <li><a href="/quarterly/transaction">Monthly Summary for the Quarter</a></li>

                    <li><a href="/list/transaction/100">List_Recent100</a></li>
                    <li><a href="/fullList/transaction">List</a></li>
                    <li><a href="/searchEntity/transaction">Search Transaction</a></li>
                    <form action="/search" method="POST" class="navbar-form navbar-left" role="search">
                                <div class="form-group">
                                  <input type="hidden" name="entity" value="transaction">
                                  <input type="text" class="form-control" placeholder="search..." name="query">
                                </div>
                                <button type="submit" class="btn btn-default">Submit</button>
                    </form>
                </ul>
              </li>
              <li role="presentation" class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                  Customer Payment<span class="caret"></span>
                </a>
                <ul class="dropdown-menu" aria-labelledby="dLabel">
                    <#if trial??>
                    <#else>
                    <li><a href="/add/payment">Add</a></li>
                    <#if admin??>
                    <li><a href="/remove/payment">Remove</a></li>
                    </#if>
                    </#if>
                    <li><a href="/last7days/payment">Last 7 days Summary</a></li>
                    <li><a href="/last30days/payment">Last 30 days Summary</a></li>
                    <li><a href="/quarterly/payment">Monthly Summary for the Quarter</a></li>

                    <li><a href="/list/payment/100">List_Recent100</a></li>
                    <li><a href="/fullList/payment">List</a></li>
                    <li><a href="/searchEntity/payment">Search Payment</a></li>
                    <form action="/search" method="POST" class="navbar-form navbar-left" role="search">
                                <div class="form-group">
                                  <input type="hidden" name="entity" value="payment">
                                  <input type="text" class="form-control" placeholder="search..." name="query">
                                </div>
                                <button type="submit" class="btn btn-default">Submit</button>
                    </form>
                </ul>
              </li>

              <li role="presentation" class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                  Customer<span class="caret"></span>
                </a>
                <ul class="dropdown-menu" aria-labelledby="dLabel">
                    <#if trial??>
                    <#else>
                    <li><a href="/add/customer">Add</a></li>

                    <#if admin??>
                    <li><a href="/remove/customer">Remove</a></li>
                    </#if>
                    </#if>
                    <li><a href="/list/customer/100">List_Recent100</a></li>
                    <li><a href="/fullList/customer">List</a></li>
                    <li><a href="/searchEntity/customer">Search Customer</a></li>
                    <form action="/search" method="POST" class="navbar-form navbar-left" role="search">
                                <div class="form-group">
                                  <input type="hidden" name="entity" value="customer">
                                  <input type="text" class="form-control" placeholder="search..." name="query">
                                </div>
                                <button type="submit" class="btn btn-default">Submit</button>
                    </form>
                </ul>
              </li>

              <li role="presentation" class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                  Salesman<span class="caret"></span>
                </a>
                <ul class="dropdown-menu" aria-labelledby="dLabel">
                    <#if trial??>
                    <#else>
                    <li><a href="/add/salesman">Add</a></li>

                    <#if admin??>
                    <li><a href="/remove/salesman">Remove</a></li>
                    </#if>
                    </#if>
                    <li><a href="/list/salesman/100">List_Recent100</a></li>
                    <li><a href="/fullList/salesman">List</a></li>
                    <li><a href="/searchEntity/salesman">Search Salesman</a></li>
                    <form action="/search" method="POST" class="navbar-form navbar-left" role="search">
                                <div class="form-group">
                                  <input type="hidden" name="entity" value="salesman">
                                  <input type="text" class="form-control" placeholder="search..." name="query">
                                </div>
                                <button type="submit" class="btn btn-default">Submit</button>
                    </form>
                </ul>
              </li>
              <li role="presentation" class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                  Company<span class="caret"></span>
                </a>
                <ul class="dropdown-menu" aria-labelledby="dLabel">
                    <#if trial??>
                    <#else>
                    <li><a href="/add/company">Add</a></li>

                    <#if admin??>
                    <li><a href="/remove/company">Remove</a></li>
                    </#if>
                    </#if>
                    <li><a href="/list/company/100">List_Recent100</a></li>
                    <li><a href="/fullList/company">List</a></li>
                    <li><a href="/searchEntity/company">Search Company</a></li>
                    <form action="/search" method="POST" class="navbar-form navbar-left" role="search">
                                <div class="form-group">
                                  <input type="hidden" name="entity" value="company">
                                  <input type="text" class="form-control" placeholder="search..." name="query">
                                </div>
                                <button type="submit" class="btn btn-default">Submit</button>
                    </form>
                </ul>
              </li>


              <form action="/multiSearch" method="POST" class="navbar-form navbar-left" role="search">
                             <div class="form-group">
                             Search Everything:  <input type="text" class="form-control" placeholder="search..." name="query">
                             </div>
                             <button type="submit" class="btn btn-default">Submit</button>
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
      <#include "/common/keyboard.ftl">
      <#if username??>
      <table class="table">
              <tr>
              <td>Add a Customer Transaction: <a href="/add/transaction"><i class="fa fa-plus-square fa-lg" aria-hidden="true"></i></a><span class="tips" style="display:none; color:green " ><b><i> { ctrl t }</i></b></span></td>
              <td>Search for Customer Transaction: <a href="/searchEntity/transaction"><i class="fa fa-search fa-lg" aria-hidden="true"></i></a><span class="tips" style="display:none; color:green " ><b><i> { ctrl shift t }</i></b></span></td>
              </tr>
              <tr>
              <td>Add a Customer Payment: <a href="/add/payment"><i class="fa fa-plus-square fa-lg" aria-hidden="true"></i></a><span class="tips" style="display:none; color:green  " ><b><i> { ctrl k }</i></b></span></td>
              <td>Search for Customer Payment: <a href="/searchEntity/payment"><i class="fa fa-search fa-lg" aria-hidden="true"></i></a><span class="tips" style="display:none; color:green " ><b><i> { ctrl shift k }</i></b></span></td>
              </tr>
              <tr>

              <td>Add Inventory: <a href="/add/item"><i class="fa fa-plus-square fa-lg" aria-hidden="true"></i></a><span class="tips" style="display:none; color:green " ><b><i> { ctrl j }</i></b></span></td>
              <td>Search Customer : <a href="/searchEntity/customer"><i class="fa fa-search fa-lg" aria-hidden="true"></i></a><span class="tips" style="display:none;color:green  " ><b><i> { ctrl shift j }</i></b></span></td>
              </tr>
              <tr>
              <td>Daily Summary: <a href="/last7days"><i class="fa fa-line-chart fa-lg" aria-hidden="true"></i></a><span class="tips" style="display:none;color:green  " ><b><i> { ctrl 1 }</i></b></span></td>
              <td>Monthly Summary: <a href="/quarterly"><i class="fa fa-line-chart fa-lg" aria-hidden="true"></i></a><span class="tips" style="display:none; color:green " ><b><i> { ctrl 2 }</i></b></span></td>
              </tr>
      </table>
      </#if>
  </div>
