# ![Flashcards app](img/logo_readme.png) Flashcards App - User Guide

## Application Overview

The Flashcards application is a learning tool designed to help users memorize information through question-and-answer cards organized into categories.

															  
Each card contains a question and a hidden answer. Users can reveal the answer by clicking on the question. A **Copy answer** button then becomes available.

You can add, edit, and delete categories or flashcards at any time.

> The application uses the full CRUD API described [here](../readme.md#api-base-urls).

> A sample dataset containing five categories and five associated flashcards [is available](../readme.md#initialize-the-database).
> Alternatively, you can start with an empty database and create your own library.

<details>
  <summary>🔍 Click to see the full screenshot</summary>
  <br>
  <a href="img/01-overview.png">
    <img src="img/01-overview.png" alt="Full application screenshot" width="100%">
  </a>
</details>

---

Here is a short scenario to demonstrate how the application works.

## Filter Flashcards by Category

> Clicking on a category automatically fills in the Category search field.

																			 

The results display all flashcards associated with the selected category.

![Clicking on a category automatically fills in the search field](img/1-click-category.png)

## Edit a Category

> Categories can be renamed at any time.

Click the **Edit** button (pen icon) next to the category.

Rename the category from `Git short-liner` to `Git one-liner`, then click the **Update** button to save your changes. You can also cancel the operation.

![Renaming a category](img/2-edit-category.png)

The category name is immediately updated on all associated flashcards.

![Updated category name on associated flashcards](img/3-category-updated.png)

## Search for Flashcards by Keyword

> The application can return multiple matching flashcards.

Enter the keyword `remove` in the search field.

The results include two matching flashcards:
- One from the **Git** category.
- One from the **Vagrant** category.

![Searching for flashcards using a keyword](img/5-search-keyword-remove-2results.png)

### Refine the Search Using a Category Filter

Enter part of a category name, such as `vag`, in the Category field.

The results are filtered further, and only one flashcard remains: the matching flashcard from the **Vagrant** category.

![Filtering search results by category](img/6-search-all-auto-completion-result.png)

## Create a Category

> Categories allow flashcards to be grouped by topic.

Enter `Github Actions` in the **Category name** field and click the **Create** button.

![Creating a category](img/7-create-category-github-actions.png)

The new category immediately appears in the category list.


## Create a Flashcard

> A flashcard consists of a question, an answer, and an associated category.

Enter the question, the answer, select the category, and click on the button **+ Create Flashcard**

![Creating a flashcard with a question, answer, and category](img/8-create-flashcards-github-actions.png)

A notification confirms that the flashcard has been created.

If it is not immediately visible because there are many entries, click on its category to find it.

## Search Across Multiple Categories

> The category search supports partial matching.

Example with **Git one-liner** category and the new one created and called **Github Actions**, the results show all these matches.

You can type a few letters of a category name to display matching results and autocomplete suggestions.

![Searching across multiple categories](img/9-search-git.png)

## Reveal and Copy a Flashcard Answer

> Flashcard answers are hidden by default. This allows users to test their knowledge before revealing the answer.

Click on the question title to reveal the answer. A **Copy answer** button becomes available.

![Revealing a flashcard answer and copying it](img/11-copy-answer.png)

A notification confirms that the answer has been copied successfully.

You can now paste it directly into your workflow, terminal, or another tool.

## Edit a Flashcard

For example, find the flashcard in the **Github Actions** category.

Click the **Edit** button and modify the question, answer, or category. Click **Save Changes**.

![Editing a flashcard](img/10-update-flashcards.png)

## Remove a Flashcard or a Category

The application supports two deletion operations.
																																		

### Delete a Flashcard

Deleting a flashcard removes only the selected flashcard.

Other flashcards in the same category remain unchanged.

![Flashcard deletion confirmation](img/12-delete-flashcard-pop-up.png)

### Delete a Category

Deleting a category also deletes all flashcards associated with it.

Before deleting a category, the application displays a warning to inform the user of the consequences.
	 
**Warning:** This action cannot be undone.

It is recommended to delete or move the flashcards to another category before deleting a category.

![Category deletion warning](img/13-delete-category-pop-up.png)

---

> Flashcards help you learn at your own pace!
