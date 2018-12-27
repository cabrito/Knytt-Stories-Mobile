
# Knytt Stories Map.bin File Format

Contained within a `.knytt.bin` archive is a file titled `Map.bin`. Within this file is all of the data relevant for constructing any individual `Submap` that appears in-game. Our analysis hereon will use the `Map.bin` file from the KS pack-in Level, **The Machine** by Nifflas. However, it should be noted that any Knytt Stories level will follow the same structure.

### Definitions

For the following analysis, we make use of several terms. Here are some of the important definitions for comprehension of this tutorial.

-   **Submap**: any individual overworld screen in Knytt Stories, consisting of 25 tiles by 10 tiles. Typically contains scenery and objects such as enemies and power-ups.

## Reading of Map.bin

The contents of `Map.bin` are GZip compressed. Upon decompression, the structure of each `Submap` in the file becomes apparent:



Test. This should be live. You can use the [editor on GitHub](https://github.com/scalrx/scalrx.github.io/edit/master/index.md) to maintain and preview the content for your website in Markdown files.

Whenever you commit to this repository, GitHub Pages will run [Jekyll](https://jekyllrb.com/) to rebuild the pages in your site, from the content in your Markdown files.

### Markdown

Markdown is a lightweight and easy-to-use syntax for styling your writing. It includes conventions for

```markdown
Syntax highlighted code block

# Header 1
## Header 2
### Header 3

- Bulleted
- List

1. Numbered
2. List

**Bold** and _Italic_ and `Code` text

[Link](url) and ![Image](src)
```

For more details see [GitHub Flavored Markdown](https://guides.github.com/features/mastering-markdown/).

### Jekyll Themes

Your Pages site will use the layout and styles from the Jekyll theme you have selected in your [repository settings](https://github.com/scalrx/scalrx.github.io/settings). The name of this theme is saved in the Jekyll `_config.yml` configuration file.

### Support or Contact

Having trouble with Pages? Check out our [documentation](https://help.github.com/categories/github-pages-basics/) or [contact support](https://github.com/contact) and we’ll help you sort it out.
